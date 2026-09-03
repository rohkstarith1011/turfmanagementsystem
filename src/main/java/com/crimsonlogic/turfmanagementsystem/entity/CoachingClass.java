package com.crimsonlogic.turfmanagementsystem.entity;



import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

@Entity
@Table(name = "coaching_classes")
public class CoachingClass {

    @Id
    @Column(name = "coaching_class_id", nullable = false, unique = true)
    private String coachingClassId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turf_sport_id", nullable = false)
    private TurfSport turfSport;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "class_type", nullable = false)
    private String classType;

    @Column(name = "registration_limit", nullable = false)
    private Integer registrationLimit;

    @Column(name = "fee", nullable = false)
    private Double fee;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false)
    private String status;

    public CoachingClass() {
    }

    public CoachingClass(String coachingClassId, Coach coach, TurfSport turfSport,
                          String name, String description, String classType,
                          Integer registrationLimit, Double fee,
                          LocalDateTime startDate, LocalDateTime endDate,
                          String status) {
        this.coachingClassId = coachingClassId;
        this.coach = coach;
        this.turfSport = turfSport;
        this.name = name;
        this.description = description;
        this.classType = classType;
        this.registrationLimit = registrationLimit;
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getCoachingClassId() {
        return coachingClassId;
    }

    public void setCoachingClassId(String coachingClassId) {
        this.coachingClassId = coachingClassId;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public TurfSport getTurfSport() {
        return turfSport;
    }

    public void setTurfSport(TurfSport turfSport) {
        this.turfSport = turfSport;
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
    @PrePersist
    private void generateCoachingClassId() {
        if (coachingClassId == null || coachingClassId.isBlank()) {
            coachingClassId = EntityIdGenerator.generate("COC");
        }
    }
}
