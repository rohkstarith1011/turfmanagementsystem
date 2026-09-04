package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



public class PlayingAreaResponseDTO {

    private String playingAreaId;

    private String facilityId;

    private String turfSportId;

    private String name;

    private String description;

    private String status;

    // Getters and Setters

    public String getPlayingAreaId() {
        return playingAreaId;
    }

    public void setPlayingAreaId(String playingAreaId) {
        this.playingAreaId = playingAreaId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getTurfSportId() {
        return turfSportId;
    }

    public void setTurfSportId(String turfSportId) {
        this.turfSportId = turfSportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
