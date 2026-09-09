package com.crimsonlogic.turfmanagementsystem.repository.projection;

public interface OwnerPerformanceProjection {

    String getOwnerName();

    String getFacilityName();

    Long getTotalBookings();

    Double getRevenue();
}