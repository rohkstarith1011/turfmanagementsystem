package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;

public class TurfAmenityRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotBlank(message = "Amenity ID is required")
    private String amenityId;

    // Getters and Setters

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(String amenityId) {
        this.amenityId = amenityId;
    }
}
