package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.ai.BaseAiService;
import com.crimsonlogic.turfmanagementsystem.ai.OllamaClient;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotRecommendationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiSlotRecommendationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.RecommendedSlotDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Slot;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiSlotRecommendationService extends BaseAiService {

    private final SlotRepository slotRepository;

    public AiSlotRecommendationService(OllamaClient ollamaClient, ObjectMapper objectMapper,
                                       SlotRepository slotRepository) {
        super(ollamaClient, objectMapper);
        this.slotRepository = slotRepository;
    }

    public AiSlotRecommendationResponseDTO recommendSlots(SlotRecommendationRequestDTO requestDTO) {
        // 1. Fetch available candidate slots
        List<Slot> candidates = slotRepository.findAvailableSlotsByFacilityAndDate(
                requestDTO.getFacilityId(), requestDTO.getTargetDate());

        if (candidates.isEmpty()) {
            AiSlotRecommendationResponseDTO emptyResponse = new AiSlotRecommendationResponseDTO();
            emptyResponse.setRecommendations(new ArrayList<>());
            return emptyResponse;
        }

        Map<String, Slot> candidateMap = new HashMap<>();
        for (Slot s : candidates) {
            candidateMap.put(s.getSlotId(), s);
        }

        // 2. Build Prompt
        String prompt = buildPrompt(requestDTO, candidates);

        // 3. Call AI
        AiSlotRecommendationResponseDTO aiResponse = executePrompt(prompt, AiSlotRecommendationResponseDTO.class);

        // 4. Validate output
        List<RecommendedSlotDTO> validatedRecommendations = new ArrayList<>();
        if (aiResponse.getRecommendations() != null) {
            for (RecommendedSlotDTO aiRec : aiResponse.getRecommendations()) {
                if (candidateMap.containsKey(aiRec.getSlotId())) {
                    Slot actualSlot = candidateMap.get(aiRec.getSlotId());
                    
                    // Override with genuine database properties
                    aiRec.setPlayingAreaName(actualSlot.getPlayingArea().getName());
                    aiRec.setStartTime(actualSlot.getStartTime().toString());
                    aiRec.setEndTime(actualSlot.getEndTime().toString());
                    
                    validatedRecommendations.add(aiRec);
                }
            }
        }

        AiSlotRecommendationResponseDTO finalResponse = new AiSlotRecommendationResponseDTO();
        finalResponse.setRecommendations(validatedRecommendations);
        return finalResponse;
    }

    private String buildPrompt(SlotRecommendationRequestDTO request, List<Slot> candidates) {
        try {
            List<Map<String, Object>> candidateData = new ArrayList<>();
            for (Slot s : candidates) {
                Map<String, Object> map = new HashMap<>();
                map.put("slotId", s.getSlotId());
                map.put("startTime", s.getStartTime().toString());
                map.put("endTime", s.getEndTime().toString());
                map.put("playingAreaName", s.getPlayingArea().getName());
                candidateData.add(map);
            }

            String candidatesJson = objectMapper.writeValueAsString(candidateData);

            return "You are an AI Slot Recommendation engine for a turf management system.\n" +
                   "A user wants to book a slot with these preferences:\n" +
                   "Preferred Time of Day: " + (request.getPreferredTimeOfDay() != null ? request.getPreferredTimeOfDay() : "Any") + "\n" +
                   "Preferred Duration (hours): " + (request.getDurationHours() != null ? request.getDurationHours() : "Any") + "\n\n" +
                   "Here are the strictly AVAILABLE candidate slots for the selected facility and date:\n" +
                   candidatesJson + "\n\n" +
                   "Analyze these candidates and rank them from best to worst match based on the user's preferences.\n" +
                   "Output ONLY valid JSON strictly matching this structure, with no markdown formatting or extra text:\n" +
                   "{\n" +
                   "  \"recommendations\": [\n" +
                   "    {\n" +
                   "      \"slotId\": \"id\",\n" +
                   "      \"matchScore\": 95,\n" +
                   "      \"reasons\": [\"reason 1\", \"reason 2\"]\n" +
                   "    }\n" +
                   "  ]\n" +
                   "}";

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to construct AI prompt", e);
        }
    }
}
