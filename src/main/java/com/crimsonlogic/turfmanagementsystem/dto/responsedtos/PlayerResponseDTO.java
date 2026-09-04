package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

public class PlayerResponseDTO {

    private String playerId;

    private String userId;

    private String name;

    private String email;

    private String phone;

    private UserStatus status;

    private String locality;

    private String skillLevel;

    private String preferredSports;

    // Getters and Setters

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getPreferredSports() {
        return preferredSports;
    }

    public void setPreferredSports(String preferredSports) {
        this.preferredSports = preferredSports;
    }
}