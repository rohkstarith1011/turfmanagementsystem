package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.ai.BaseAiService;
import com.crimsonlogic.turfmanagementsystem.ai.OllamaClient;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PricingApprovalRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PricingSimulationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiPricingSimulationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.AuditLog;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.exception.AiIntegrationException;
import com.crimsonlogic.turfmanagementsystem.exception.BadRequestException;
import com.crimsonlogic.turfmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.turfmanagementsystem.repository.AuditLogRepository;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.crimsonlogic.turfmanagementsystem.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AiDynamicPricingService extends BaseAiService {

    private final FacilityRepository facilityRepository;
    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final AuditLogRepository auditLogRepository;

    public AiDynamicPricingService(OllamaClient ollamaClient, ObjectMapper objectMapper,
                                   FacilityRepository facilityRepository,
                                   BookingRepository bookingRepository,
                                   SlotRepository slotRepository,
                                   AuditLogRepository auditLogRepository) {
        super(ollamaClient, objectMapper);
        this.facilityRepository = facilityRepository;
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public AiPricingSimulationResponseDTO simulatePricing(PricingSimulationRequestDTO requestDTO) {
        Facility facility = facilityRepository.findById(requestDTO.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        verifyOwnership(facility);

        if (requestDTO.getMinimumPrice() > requestDTO.getMaximumPrice()) {
            throw new BadRequestException("Minimum price cannot be greater than maximum price");
        }

        // Calculate demand context (similar to Demand Forecasting but for general facility health)
        LocalDate today = LocalDate.now();
        Long totalSlots = slotRepository.countActiveSlotsByFacilityAndDate(facility.getFacilityId(), today);
        Long past30DaysBookings = bookingRepository.countBookingsByFacilityAndBookingDateBetween(
                facility.getFacilityId(), today.minusDays(30), today);
        double dailyAverage = (past30DaysBookings != null ? past30DaysBookings : 0) / 30.0;
        double currentOccupancy = totalSlots != null && totalSlots > 0 ? (dailyAverage / totalSlots) * 100 : 0;

        String prompt = buildSimulationPrompt(requestDTO, facility.getBasePrice(), currentOccupancy);

        AiPricingSimulationResponseDTO aiResponse = executePrompt(prompt, AiPricingSimulationResponseDTO.class);

        // Strict Validation (Section 27): Reject if AI ignores pricing limits
        if (aiResponse.getRecommendedPrice() == null || 
            aiResponse.getRecommendedPrice() < requestDTO.getMinimumPrice() || 
            aiResponse.getRecommendedPrice() > requestDTO.getMaximumPrice()) {
            
            throw new AiIntegrationException("AI recommended a price out of the required bounds. " +
                    "Recommended: " + aiResponse.getRecommendedPrice() + ", Bounds: [" +
                    requestDTO.getMinimumPrice() + ", " + requestDTO.getMaximumPrice() + "]");
        }

        // Enforce base price accuracy
        aiResponse.setBasePrice(facility.getBasePrice());

        return aiResponse;
    }

    @Transactional
    public void approvePricing(PricingApprovalRequestDTO requestDTO) {
        Facility facility = facilityRepository.findById(requestDTO.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        CustomUserDetails currentUser = verifyOwnership(facility);

        Double oldPrice = facility.getBasePrice();
        Double newPrice = requestDTO.getApprovedPrice();

        facility.setBasePrice(newPrice);
        facilityRepository.save(facility);

        // Create Audit Log (Section 26 & 14)
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("PRICE_APPROVAL");
        auditLog.setEntityType("FACILITY");
        auditLog.setEntityId(facility.getFacilityId());
        auditLog.setOldValue(String.valueOf(oldPrice));
        auditLog.setNewValue(String.valueOf(newPrice));
        auditLog.setPerformedBy(currentUser.getUser());
        auditLog.setTimestamp(LocalDateTime.now());
        
        auditLogRepository.save(auditLog);
    }

    private String buildSimulationPrompt(PricingSimulationRequestDTO request, Double currentPrice, double occupancyPercentage) {
        return "You are an AI Dynamic Pricing Simulator.\n" +
               "A facility owner wants to optimize their base price based on current demand.\n\n" +
               "Facility Context:\n" +
               "- Current Base Price: " + currentPrice + "\n" +
               "- Recent Occupancy Average: " + String.format("%.1f", occupancyPercentage) + "%\n" +
               "- Owner's Minimum Acceptable Price: " + request.getMinimumPrice() + "\n" +
               "- Owner's Maximum Acceptable Price: " + request.getMaximumPrice() + "\n\n" +
               "Recommend a new base price. The recommended price MUST be strictly between the Minimum and Maximum price bounds.\n" +
               "Output ONLY valid JSON strictly matching this structure, with no markdown formatting or extra text:\n" +
               "{\n" +
               "  \"recommendedPrice\": 150.0,\n" +
               "  \"demandLevel\": \"HIGH\",\n" +
               "  \"factors\": [\"High recent occupancy justifies a price increase\"]\n" +
               "}";
    }

    private CustomUserDetails verifyOwnership(Facility facility) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                return userDetails;
            }
            
            String currentUserId = userDetails.getUser().getUserId();
            // Assuming TurfOwner and TurfManager can both simulate
            boolean isOwner = facility.getOwner() != null && facility.getOwner().getUser().getUserId().equals(currentUserId);
            boolean isManager = facility.getManager() != null && facility.getManager().getUser().getUserId().equals(currentUserId);
            
            if (!isOwner && !isManager) {
                throw new AccessDeniedException("You do not have permission to manage pricing for this facility.");
            }
            return userDetails;
        }
        throw new AccessDeniedException("Authentication required.");
    }
}
