package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CoachingClassRequestDTO {

    @NotBlank(message = "Coach ID is required")
    private String coachId;

    @NotBlank(message = "Turf Sport ID is required")
    private String turfSportId;

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 150, message = "Class name must be between 2 and 150 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Class type is required")
    @Size(max = 100, message = "Class type cannot exceed 100 characters")
    private String classType;

    @NotNull(message = "Registration limit is required")
    @Min(value = 1, message = "Registration limit must be at least 1")
    private Integer registrationLimit;

    @NotNull(message = "Fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee cannot be negative")
    private Double fee;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    // Getters and Setters

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Integer getRegistrationLimit() {
        return registrationLimit;
    }

    public void setRegistrationLimit(Integer registrationLimit) {
        this.registrationLimit = registrationLimit;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}