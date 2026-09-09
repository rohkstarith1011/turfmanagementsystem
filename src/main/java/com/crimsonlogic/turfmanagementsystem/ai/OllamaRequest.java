package com.crimsonlogic.turfmanagementsystem.ai;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OllamaRequest {
    private String model;
    private String prompt;
    private boolean stream = false;
    private String format = "json"; // Enforce JSON response

    public OllamaRequest(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
    }
}
