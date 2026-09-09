package com.crimsonlogic.turfmanagementsystem.ai;

import com.crimsonlogic.turfmanagementsystem.exception.AiIntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class OllamaClient {

    private final RestTemplate restTemplate;

    @Value("${ollama.model:llama3}")
    private String model;

    public OllamaClient(
            @Value("${ollama.base-url:http://localhost:11434}") String baseUrl,
            @Value("${ollama.timeout:30000}") long timeoutMillis) {
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) timeoutMillis);
        factory.setReadTimeout((int) timeoutMillis);
        
        this.restTemplate = new RestTemplate(factory);
        this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
    }

    public String generate(String prompt) {
        OllamaRequest requestDto = new OllamaRequest(model, prompt);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<OllamaRequest> entity = new HttpEntity<>(requestDto, headers);
        
        try {
            ResponseEntity<OllamaResponse> response = restTemplate.postForEntity(
                    "/api/generate", entity, OllamaResponse.class);
                    
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getResponse();
            } else {
                throw new AiIntegrationException("Ollama returned non-success status: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new AiIntegrationException("Failed to communicate with Ollama: " + e.getMessage(), e);
        }
    }
}
