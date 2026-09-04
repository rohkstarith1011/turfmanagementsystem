package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeamRequestDTO {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 100, message = "Team name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Team description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Sport ID is required")
    private String sportId;

    @NotBlank(message = "Creator player ID is required")
    private String createdBy;

    // Getters and Setters

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

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}