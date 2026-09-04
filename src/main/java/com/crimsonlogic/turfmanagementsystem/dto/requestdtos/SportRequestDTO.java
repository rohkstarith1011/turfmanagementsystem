package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SportRequestDTO {

    @NotBlank(message = "Sport name is required")
    @Size(min = 2, max = 100, message = "Sport name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Sport description is required")
    @Size(max = 500, message = "Sport description cannot exceed 500 characters")
    private String description;

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
}