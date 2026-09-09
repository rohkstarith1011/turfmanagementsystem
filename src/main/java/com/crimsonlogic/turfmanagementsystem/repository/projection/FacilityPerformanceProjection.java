package com.crimsonlogic.turfmanagementsystem.repository.projection;

public interface FacilityPerformanceProjection {

    String getFacilityName();

    String getLocation();

    Long getTotalBookings();

    Double getRevenue();

    Double getRating();
}