package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;


import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

public class TurfOwnerResponseDTO {

    private String turfOwnerId;

    private String userId;

    private String name;

    private String email;

    private String phone;

    private UserStatus status;

    // Getters and Setters

    public String getTurfOwnerId() {
        return turfOwnerId;
    }

    public void setTurfOwnerId(String turfOwnerId) {
        this.turfOwnerId = turfOwnerId;
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
}
