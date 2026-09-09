package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SlotRecommendationRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotNull(message = "Target date is required")
    private LocalDate targetDate;

    private String preferredTimeOfDay; // e.g., "Morning", "Evening"
    
    private Integer durationHours;
}
