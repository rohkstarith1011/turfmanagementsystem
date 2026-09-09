package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.ai.BaseAiService;
import com.crimsonlogic.turfmanagementsystem.ai.OllamaClient;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.DemandForecastRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiDemandForecastResponseDTO;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AiDemandForecastService extends BaseAiService {

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;

    public AiDemandForecastService(OllamaClient ollamaClient, ObjectMapper objectMapper,
                                   BookingRepository bookingRepository,
                                   SlotRepository slotRepository) {
        super(ollamaClient, objectMapper);
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
    }

    public AiDemandForecastResponseDTO forecastDemand(DemandForecastRequestDTO requestDTO) {
        String facilityId = requestDTO.getFacilityId();
        LocalDate targetDate = requestDTO.getTargetDate();

        // 1. Calculate Capacity
        Long totalSlots = slotRepository.countActiveSlotsByFacilityAndDate(facilityId, targetDate);
        if (totalSlots == null || totalSlots == 0) {
            AiDemandForecastResponseDTO zeroResponse = new AiDemandForecastResponseDTO();
            zeroResponse.setDemandLevel("LOW");
            zeroResponse.setExpectedOccupancyPercentage(0);
            zeroResponse.setExplanation("The facility has no active slots on this date, resulting in zero capacity and no demand.");
            return zeroResponse;
        }

        // 2. Calculate 30-day baseline
        LocalDate today = LocalDate.now();
        Long past30DaysBookings = bookingRepository.countBookingsByFacilityAndBookingDateBetween(
                facilityId, today.minusDays(30), today);
        double dailyAverage = (past30DaysBookings != null ? past30DaysBookings : 0) / 30.0;

        // 3. Calculate 4-week day-of-week trend
        long past4WeeksSameDayBookings = 0;
        for (int i = 1; i <= 4; i++) {
            LocalDate historicalDay = targetDate.minusWeeks(i);
            Long dailyCount = bookingRepository.countBookingsByFacilityAndDate(facilityId, historicalDay);
            past4WeeksSameDayBookings += (dailyCount != null ? dailyCount : 0);
        }
        double dayOfWeekAverage = past4WeeksSameDayBookings / 4.0;

        // 4. Build Prompt
        String prompt = buildPrompt(requestDTO, totalSlots, dailyAverage, dayOfWeekAverage);

        // 5. Call AI
        AiDemandForecastResponseDTO aiResponse = executePrompt(prompt, AiDemandForecastResponseDTO.class);

        // 6. Post-validate AI results
        if (aiResponse.getExpectedOccupancyPercentage() == null) {
            aiResponse.setExpectedOccupancyPercentage(0);
        } else if (aiResponse.getExpectedOccupancyPercentage() < 0) {
            aiResponse.setExpectedOccupancyPercentage(0);
        } else if (aiResponse.getExpectedOccupancyPercentage() > 100) {
            aiResponse.setExpectedOccupancyPercentage(100);
        }

        String level = aiResponse.getDemandLevel();
        if (level == null || (!level.equalsIgnoreCase("LOW") && !level.equalsIgnoreCase("MEDIUM") && !level.equalsIgnoreCase("HIGH"))) {
            // Default to medium if hallucinated
            if (aiResponse.getExpectedOccupancyPercentage() > 70) {
                aiResponse.setDemandLevel("HIGH");
            } else if (aiResponse.getExpectedOccupancyPercentage() < 30) {
                aiResponse.setDemandLevel("LOW");
            } else {
                aiResponse.setDemandLevel("MEDIUM");
            }
        } else {
            aiResponse.setDemandLevel(level.toUpperCase());
        }

        return aiResponse;
    }

    private String buildPrompt(DemandForecastRequestDTO request, Long totalSlots, 
                               double dailyAverage, double dayOfWeekAverage) {
        return "You are an AI Demand Forecasting engine for a turf management system.\n" +
               "A facility owner wants to forecast demand for a specific future date.\n\n" +
               "Here is the strict statistical context for the facility:\n" +
               "- Target Date: " + request.getTargetDate() + " (" + request.getTargetDate().getDayOfWeek() + ")\n" +
               "- Total Daily Slot Capacity: " + totalSlots + " slots\n" +
               "- 30-Day Moving Average Bookings: " + String.format("%.2f", dailyAverage) + " bookings/day\n" +
               "- 4-Week Average for this specific Day of Week (" + request.getTargetDate().getDayOfWeek() + "): " + String.format("%.2f", dayOfWeekAverage) + " bookings/day\n\n" +
               "External factors specified by the owner:\n" +
               "- Is a Holiday: " + (Boolean.TRUE.equals(request.getIsHoliday()) ? "Yes" : "No") + "\n" +
               "- Local Events: " + (request.getLocalEvents() != null && !request.getLocalEvents().isBlank() ? request.getLocalEvents() : "None") + "\n\n" +
               "Based purely on the math and external factors, predict the demand.\n" +
               "Output ONLY valid JSON strictly matching this structure, with no markdown formatting or extra text:\n" +
               "{\n" +
               "  \"demandLevel\": \"HIGH\", // must be exactly LOW, MEDIUM, or HIGH\n" +
               "  \"expectedOccupancyPercentage\": 85, // integer 0-100\n" +
               "  \"explanation\": \"Detailed reasoning explaining the calculation and impact of events.\"\n" +
               "}";
    }
}
