package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;


import jakarta.validation.constraints.NotBlank;

public class CoachingClassRegistrationRequestDTO {

    @NotBlank(message = "Coaching class ID is required")
    private String coachingClassId;

    @NotBlank(message = "Player ID is required")
    private String playerId;

    // Getters and Setters

    public String getCoachingClassId() {
        return coachingClassId;
    }

    public void setCoachingClassId(String coachingClassId) {
        this.coachingClassId = coachingClassId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
