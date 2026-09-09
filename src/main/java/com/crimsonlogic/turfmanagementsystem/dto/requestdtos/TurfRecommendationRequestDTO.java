package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TurfRecommendationRequestDTO {

    @NotBlank(message = "City is required for recommendation")
    private String city;

    private String sportId;

    private Double maxBudget;

    private String preferredTime;

    private Integer groupSize;
}
