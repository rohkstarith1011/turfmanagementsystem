package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CancellationPolicyRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotBlank(message = "Configured by user ID is required")
    private String configuredByUserId;

    @NotNull(message = "Full refund hours are required")
    @Min(value = 1, message = "Full refund hours must be greater than 0")
    private Integer fullRefundHours;

    @NotNull(message = "Partial refund hours are required")
    @Min(value = 1, message = "Partial refund hours must be greater than 0")
    private Integer partialRefundHours;

    @NotNull(message = "Limited refund hours are required")
    @Min(value = 1, message = "Limited refund hours must be greater than 0")
    private Integer limitedRefundHours;

    @NotNull(message = "Full refund percentage is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double fullRefundPercentage;

    @NotNull(message = "Partial refund percentage is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double partialRefundPercentage;

    @NotNull(message = "Limited refund percentage is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double limitedRefundPercentage;

    @NotNull(message = "Minimum refund percentage is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double minimumRefundPercentage;

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getConfiguredByUserId() {
        return configuredByUserId;
    }

    public void setConfiguredByUserId(String configuredByUserId) {
        this.configuredByUserId = configuredByUserId;
    }

    public Integer getFullRefundHours() {
        return fullRefundHours;
    }

    public void setFullRefundHours(Integer fullRefundHours) {
        this.fullRefundHours = fullRefundHours;
    }

    public Integer getPartialRefundHours() {
        return partialRefundHours;
    }

    public void setPartialRefundHours(Integer partialRefundHours) {
        this.partialRefundHours = partialRefundHours;
    }

    public Integer getLimitedRefundHours() {
        return limitedRefundHours;
    }

    public void setLimitedRefundHours(Integer limitedRefundHours) {
        this.limitedRefundHours = limitedRefundHours;
    }

    public Double getFullRefundPercentage() {
        return fullRefundPercentage;
    }

    public void setFullRefundPercentage(Double fullRefundPercentage) {
        this.fullRefundPercentage = fullRefundPercentage;
    }

    public Double getPartialRefundPercentage() {
        return partialRefundPercentage;
    }

    public void setPartialRefundPercentage(Double partialRefundPercentage) {
        this.partialRefundPercentage = partialRefundPercentage;
    }

    public Double getLimitedRefundPercentage() {
        return limitedRefundPercentage;
    }

    public void setLimitedRefundPercentage(Double limitedRefundPercentage) {
        this.limitedRefundPercentage = limitedRefundPercentage;
    }

    public Double getMinimumRefundPercentage() {
        return minimumRefundPercentage;
    }

    public void setMinimumRefundPercentage(Double minimumRefundPercentage) {
        this.minimumRefundPercentage = minimumRefundPercentage;
    }
}