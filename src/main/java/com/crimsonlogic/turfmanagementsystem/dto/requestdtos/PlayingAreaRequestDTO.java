package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PlayingAreaRequestDTO {

    @NotBlank(message = "Facility ID is required")
    private String facilityId;

    @NotBlank(message = "Turf Sport ID is required")
    private String turfSportId;

    @NotBlank(message = "Playing area name is required")
    @Size(min = 2, max = 100, message = "Playing area name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Playing area description is required")
    @Size(max = 500, message = "Playing area description cannot exceed 500 characters")
    private String description;

    // Getters and Setters

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
}