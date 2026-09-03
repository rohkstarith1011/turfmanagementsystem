package com.crimsonlogic.turfmanagementsystem.entity;



import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

@Entity
@Table(
    name = "coaching_class_registrations",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"coaching_class_id", "player_id"})
    }
)
public class CoachingClassRegistration {

    @Id
    @Column(name = "coaching_class_registration_id", nullable = false, unique = true)
    private String coachingClassRegistrationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coaching_class_id", nullable = false)
    private CoachingClass coachingClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "status", nullable = false)
    private String status;

    public CoachingClassRegistration() {
    }

    public CoachingClassRegistration(String coachingClassRegistrationId,
                                     CoachingClass coachingClass,
                                     Player player,
                                     LocalDateTime registrationDate,
                                     String status) {
        this.coachingClassRegistrationId = coachingClassRegistrationId;
        this.coachingClass = coachingClass;
        this.player = player;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public String getCoachingClassRegistrationId() {
        return coachingClassRegistrationId;
    }

    public void setCoachingClassRegistrationId(String coachingClassRegistrationId) {
        this.coachingClassRegistrationId = coachingClassRegistrationId;
    }

    public CoachingClass getCoachingClass() {
        return coachingClass;
    }

    public void setCoachingClass(CoachingClass coachingClass) {
        this.coachingClass = coachingClass;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    private void generateCoachingClassRegistrationId() {
        if (coachingClassRegistrationId == null || coachingClassRegistrationId.isBlank()) {
            coachingClassRegistrationId = EntityIdGenerator.generate("COR");
        }
    }
}