package com.crimsonlogic.turfmanagementsystem.ai;

import lombok.Data;

@Data
public class OllamaResponse {
    private String model;
    private String created_at;
    private String response;
    private boolean done;
}
