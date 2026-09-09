package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

public class FacilityPerformanceDTO {

    private String facilityName;
    private String location;
    private Long totalBookings;
    private Double revenue;
    private Double rating;

    public FacilityPerformanceDTO(
            String facilityName,
            String location,
            Long totalBookings,
            Double revenue,
            Double rating) {

        this.facilityName = facilityName;
        this.location = location;
        this.totalBookings = totalBookings;
        this.revenue = revenue;
        this.rating = rating;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getLocation() {
        return location;
    }

    public Long getTotalBookings() {
        return totalBookings;
    }

    public Double getRevenue() {
        return revenue;
    }

    public Double getRating() {
        return rating;
    }
}