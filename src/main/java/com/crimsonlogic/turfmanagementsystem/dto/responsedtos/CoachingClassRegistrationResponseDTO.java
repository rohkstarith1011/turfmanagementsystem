package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import java.time.LocalDateTime;

public class CoachingClassRegistrationResponseDTO {

	private String coachingClassRegistrationId;

    private String coachingClassId;

    private String playerId;

    private LocalDateTime registrationDate;

    private String status;

    // Getters and Setters

    public String getCoachingClassRegistrationId() {
        return coachingClassRegistrationId;
    }

    public void setCoachingClassRegistrationId(String coachingClassRegistrationId) {
        this.coachingClassRegistrationId = coachingClassRegistrationId;
    }

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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}