package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



public class TurfAmenityResponseDTO {

    private String turfAmenityId;

    private String facilityId;

    private String amenityId;

    private String status;

    // Getters and Setters

    public String getTurfAmenityId() {
        return turfAmenityId;
    }

    public void setTurfAmenityId(String turfAmenityId) {
        this.turfAmenityId = turfAmenityId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
