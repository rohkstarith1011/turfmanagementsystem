package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PricingSimulationRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotNull(message = "Minimum price limit is required")
    @Min(value = 0, message = "Minimum price cannot be negative")
    private Double minimumPrice;

    @NotNull(message = "Maximum price limit is required")
    @Min(value = 0, message = "Maximum price cannot be negative")
    private Double maximumPrice;
}
