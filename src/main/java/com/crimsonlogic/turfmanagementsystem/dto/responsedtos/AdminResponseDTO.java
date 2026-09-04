package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;


import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

public class AdminResponseDTO {

    private String adminId;

    private String userId;

    private String name;

    private String email;

    private String phone;

    private UserStatus status;

    // Getters and Setters

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
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