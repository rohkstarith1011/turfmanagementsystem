package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

public class OwnerPerformanceDTO {

    private String ownerName;
    private String facilityName;
    private Long totalBookings;
    private Double revenue;

    public OwnerPerformanceDTO(
            String ownerName,
            String facilityName,
            Long totalBookings,
            Double revenue) {
        this.ownerName = ownerName;
        this.facilityName = facilityName;
        this.totalBookings = totalBookings;
        this.revenue = revenue;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Long getTotalBookings() {
        return totalBookings;
    }

    public Double getRevenue() {
        return revenue;
    }
}