package com.crimsonlogic.turfmanagementsystem.ai;

import com.crimsonlogic.turfmanagementsystem.exception.AiIntegrationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseAiService {

    protected final OllamaClient ollamaClient;
    protected final ObjectMapper objectMapper;

    protected BaseAiService(OllamaClient ollamaClient, ObjectMapper objectMapper) {
        this.ollamaClient = ollamaClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Executes an AI prompt and attempts to parse the expected JSON response.
     */
    protected <T> T executePrompt(String prompt, Class<T> responseType) {
        String rawResponse = ollamaClient.generate(prompt);
        
        try {
            // Llama models sometimes wrap JSON in markdown blocks (```json ... ```)
            String cleanedJson = cleanJsonResponse(rawResponse);
            return objectMapper.readValue(cleanedJson, responseType);
        } catch (JsonProcessingException e) {
            throw new AiIntegrationException("Failed to parse AI response into structured format: " + e.getMessage(), e);
        }
    }

    private String cleanJsonResponse(String rawResponse) {
        if (rawResponse == null) return "{}";
        
        String cleaned = rawResponse.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        
        return cleaned.trim();
    }
}
