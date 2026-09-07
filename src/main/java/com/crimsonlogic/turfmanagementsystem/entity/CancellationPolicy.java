package com.crimsonlogic.turfmanagementsystem.entity;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.*;

@Entity
@Table(
    name = "cancellation_policies",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "facility_id")
    }
)
public class CancellationPolicy {

    @Id
    @Column(name = "policy_id", nullable = false, unique = true)
    private String policyId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false, unique = true)
    private Facility facility;

    @Column(name = "full_refund_hours", nullable = false)
    private Integer fullRefundHours;

    @Column(name = "partial_refund_hours", nullable = false)
    private Integer partialRefundHours;

    @Column(name = "limited_refund_hours", nullable = false)
    private Integer limitedRefundHours;

    @Column(name = "full_refund_percentage", nullable = false)
    private Double fullRefundPercentage;

    @Column(name = "partial_refund_percentage", nullable = false)
    private Double partialRefundPercentage;

    @Column(name = "limited_refund_percentage", nullable = false)
    private Double limitedRefundPercentage;

    @Column(name = "minimum_refund_percentage", nullable = false)
    private Double minimumRefundPercentage;

    @Column(name = "status", nullable = false)
    private String status;

    public CancellationPolicy() {
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
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

    @PrePersist
    private void generatePolicyId() {
        if (policyId == null || policyId.isBlank()) {
            policyId = EntityIdGenerator.generate("CPL");
        }
    }
}