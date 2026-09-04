package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



public class TurfSportResponseDTO {

    private String turfSportId;

    private String facilityId;

    private String sportId;

    private String status;

    // Getters and Setters

    public String getTurfSportId() {
        return turfSportId;
    }

    public void setTurfSportId(String turfSportId) {
        this.turfSportId = turfSportId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}