package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;

public class UserRoleRequestDTO {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Role ID is required")
    private String roleId;

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
