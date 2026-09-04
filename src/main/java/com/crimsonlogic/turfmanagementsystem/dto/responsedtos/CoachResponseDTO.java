package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;


import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

public class CoachResponseDTO {

    private String coachId;

    private String userId;

    private String turfSportId;

    private String name;

    private String email;

    private String phone;

    private UserStatus status;

    private String specialization;

    // Getters and Setters

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
