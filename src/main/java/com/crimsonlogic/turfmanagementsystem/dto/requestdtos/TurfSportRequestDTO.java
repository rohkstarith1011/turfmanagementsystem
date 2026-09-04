package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.NotBlank;

public class TurfSportRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotBlank(message = "Sport ID is required")
    private String sportId;

    // Getters and Setters

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }
}