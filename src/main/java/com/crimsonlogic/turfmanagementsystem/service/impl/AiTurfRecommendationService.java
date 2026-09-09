package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.ai.BaseAiService;
import com.crimsonlogic.turfmanagementsystem.ai.OllamaClient;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfRecommendationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AiTurfRecommendationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.RecommendedTurfDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.ReviewRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfSportRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiTurfRecommendationService extends BaseAiService {

    private final FacilityRepository facilityRepository;
    private final TurfSportRepository turfSportRepository;
    private final ReviewRepository reviewRepository;

    public AiTurfRecommendationService(OllamaClient ollamaClient, ObjectMapper objectMapper,
                                       FacilityRepository facilityRepository,
                                       TurfSportRepository turfSportRepository,
                                       ReviewRepository reviewRepository) {
        super(ollamaClient, objectMapper);
        this.facilityRepository = facilityRepository;
        this.turfSportRepository = turfSportRepository;
        this.reviewRepository = reviewRepository;
    }

    public AiTurfRecommendationResponseDTO recommendTurfs(TurfRecommendationRequestDTO requestDTO) {
        
        // 1. Fetch deterministic candidates
        List<Facility> rawFacilities = facilityRepository.findByCityIgnoreCase(requestDTO.getCity());
        
        List<Facility> candidateFacilities = new ArrayList<>();
        Map<String, Facility> candidateMap = new HashMap<>();

        for (Facility f : rawFacilities) {
            // Filter inactive/unavailable
            if (!"ACTIVE".equalsIgnoreCase(f.getStatus()) || !f.getAvailability()) {
                continue;
            }

            // Filter sport
            if (requestDTO.getSportId() != null && !requestDTO.getSportId().isBlank()) {
                boolean supportsSport = turfSportRepository.existsByFacilityFacilityIdAndSportSportId(
                        f.getFacilityId(), requestDTO.getSportId());
                if (!supportsSport) {
                    continue;
                }
            }

            // Filter budget
            if (requestDTO.getMaxBudget() != null) {
                if (f.getBasePrice() > requestDTO.getMaxBudget()) {
                    continue;
                }
            }

            candidateFacilities.add(f);
            candidateMap.put(f.getFacilityId(), f);
        }

        // Return empty if no deterministic matches
        if (candidateFacilities.isEmpty()) {
            AiTurfRecommendationResponseDTO emptyResponse = new AiTurfRecommendationResponseDTO();
            emptyResponse.setRecommendations(new ArrayList<>());
            return emptyResponse;
        }

        // 2. Build Prompt
        String prompt = buildPrompt(requestDTO, candidateFacilities);

        // 3. Call AI
        AiTurfRecommendationResponseDTO aiResponse = executePrompt(prompt, AiTurfRecommendationResponseDTO.class);

        // 4. Post-validate AI results (Section 30: AI Output Validation)
        List<RecommendedTurfDTO> validatedRecommendations = new ArrayList<>();
        if (aiResponse.getRecommendations() != null) {
            for (RecommendedTurfDTO aiRec : aiResponse.getRecommendations()) {
                // If AI hallucinates an ID, discard it
                if (candidateMap.containsKey(aiRec.getFacilityId())) {
                    Facility actualFacility = candidateMap.get(aiRec.getFacilityId());
                    
                    // Override with real database values
                    aiRec.setFacilityName(actualFacility.getName());
                    aiRec.setBasePrice(actualFacility.getBasePrice());
                    
                    Double rating = reviewRepository.findAverageRatingByFacilityId(actualFacility.getFacilityId());
                    aiRec.setRating(rating != null ? rating : actualFacility.getRating());
                    
                    validatedRecommendations.add(aiRec);
                }
            }
        }

        AiTurfRecommendationResponseDTO finalResponse = new AiTurfRecommendationResponseDTO();
        finalResponse.setRecommendations(validatedRecommendations);
        return finalResponse;
    }

    private String buildPrompt(TurfRecommendationRequestDTO request, List<Facility> candidates) {
        try {
            List<Map<String, Object>> candidateData = new ArrayList<>();
            for (Facility f : candidates) {
                Map<String, Object> map = new HashMap<>();
                map.put("facilityId", f.getFacilityId());
                map.put("facilityName", f.getName());
                map.put("basePrice", f.getBasePrice());
                
                Double rating = reviewRepository.findAverageRatingByFacilityId(f.getFacilityId());
                map.put("rating", rating != null ? rating : f.getRating());
                
                map.put("capacity", f.getCapacity());
                map.put("locality", f.getLocality());
                candidateData.add(map);
            }

            String candidatesJson = objectMapper.writeValueAsString(candidateData);
            
            return "You are an AI Turf Recommendation engine.\n" +
                   "A user wants to book a turf with these preferences:\n" +
                   "Preferred Time: " + (request.getPreferredTime() != null ? request.getPreferredTime() : "Any") + "\n" +
                   "Group Size: " + (request.getGroupSize() != null ? request.getGroupSize() : "Not specified") + "\n\n" +
                   "Here are the available candidate facilities that strictly meet the user's budget and sport requirements:\n" +
                   candidatesJson + "\n\n" +
                   "Analyze these candidates and rank them from best to worst match based on the user's preferences, facility capacity, and rating. " +
                   "Output ONLY valid JSON strictly matching this structure, with no markdown formatting or extra text:\n" +
                   "{\n" +
                   "  \"recommendations\": [\n" +
                   "    {\n" +
                   "      \"facilityId\": \"id\",\n" +
                   "      \"facilityName\": \"name\",\n" +
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
