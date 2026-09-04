package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;


import java.time.LocalDateTime;

public class CoachingClassResponseDTO {

    private String coachingClassId;

    private String coachId;

    private String turfSportId;

    private String name;

    private String description;

    private String classType;

    private Integer registrationLimit;

    private Double fee;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    // Getters and Setters

    public String getCoachingClassId() {
        return coachingClassId;
    }

    public void setCoachingClassId(String coachingClassId) {
        this.coachingClassId = coachingClassId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}