package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfRecommendationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiTurfRecommendationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.impl.AiTurfRecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotRecommendationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.DemandForecastRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PricingSimulationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PricingApprovalRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiSlotRecommendationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiDemandForecastResponseDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiPricingSimulationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.impl.AiSlotRecommendationService;
import com.crimsonlogic.turfmanagementsystem.service.impl.AiDemandForecastService;
import com.crimsonlogic.turfmanagementsystem.service.impl.AiDynamicPricingService;

@RestController
@RequestMapping("/api/ai")
public class AiRecommendationController {

    private final AiTurfRecommendationService aiTurfRecommendationService;
    private final AiSlotRecommendationService aiSlotRecommendationService;
    private final AiDemandForecastService aiDemandForecastService;
    private final AiDynamicPricingService aiDynamicPricingService;

    public AiRecommendationController(AiTurfRecommendationService aiTurfRecommendationService,
                                      AiSlotRecommendationService aiSlotRecommendationService,
                                      AiDemandForecastService aiDemandForecastService,
                                      AiDynamicPricingService aiDynamicPricingService) {
        this.aiTurfRecommendationService = aiTurfRecommendationService;
        this.aiSlotRecommendationService = aiSlotRecommendationService;
        this.aiDemandForecastService = aiDemandForecastService;
        this.aiDynamicPricingService = aiDynamicPricingService;
    }

    @PostMapping("/recommendations/turf")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<AiTurfRecommendationResponseDTO> recommendTurfs(
            @Valid @RequestBody TurfRecommendationRequestDTO requestDTO) {

        AiTurfRecommendationResponseDTO responseDTO = 
                aiTurfRecommendationService.recommendTurfs(requestDTO);
                
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/recommendations/slot")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<AiSlotRecommendationResponseDTO> recommendSlots(
            @Valid @RequestBody SlotRecommendationRequestDTO requestDTO) {

        AiSlotRecommendationResponseDTO responseDTO = 
                aiSlotRecommendationService.recommendSlots(requestDTO);
                
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/forecast/demand")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<AiDemandForecastResponseDTO> forecastDemand(
            @Valid @RequestBody DemandForecastRequestDTO requestDTO) {

        AiDemandForecastResponseDTO responseDTO = 
                aiDemandForecastService.forecastDemand(requestDTO);
                
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/pricing/simulate")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<AiPricingSimulationResponseDTO> simulatePricing(
            @Valid @RequestBody PricingSimulationRequestDTO requestDTO) {

        AiPricingSimulationResponseDTO responseDTO = 
                aiDynamicPricingService.simulatePricing(requestDTO);
                
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/pricing/approve")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<String> approvePricing(
            @Valid @RequestBody PricingApprovalRequestDTO requestDTO) {

        aiDynamicPricingService.approvePricing(requestDTO);
                
        return ResponseEntity.ok("Pricing updated and audit log recorded successfully.");
    }
}
