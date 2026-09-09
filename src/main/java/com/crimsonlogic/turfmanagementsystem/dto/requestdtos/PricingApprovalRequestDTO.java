package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PricingApprovalRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotNull(message = "Approved price is required")
    @Min(value = 0, message = "Approved price cannot be negative")
    private Double approvedPrice;
}
