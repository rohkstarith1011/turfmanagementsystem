package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

public class CancellationPolicyResponseDTO {

    private String policyId;
    private String facilityId;

    private Integer fullRefundHours;
    private Integer partialRefundHours;
    private Integer limitedRefundHours;

    private Double fullRefundPercentage;
    private Double partialRefundPercentage;
    private Double limitedRefundPercentage;
    private Double minimumRefundPercentage;

    private String status;

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}