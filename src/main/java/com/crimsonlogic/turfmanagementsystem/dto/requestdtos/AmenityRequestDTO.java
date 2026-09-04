package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AmenityRequestDTO {

    @NotBlank(message = "Amenity name is required")
    @Size(min = 2, max = 100, message = "Amenity name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Amenity description is required")
    @Size(max = 500, message = "Amenity description cannot exceed 500 characters")
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
