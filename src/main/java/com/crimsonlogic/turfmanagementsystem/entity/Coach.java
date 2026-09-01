package com.crimsonlogic.turfmanagementsystem.entity;



import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "coach")
public class Coach extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coach_id")
    private Long coachId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turf_sport_id", nullable = false)
    private TurfSport turfSport;

    @Column(nullable = false)
    private String specialization;

    public Coach() {
        super();
    }

    public Coach(Long coachId,
                 String name,
                 String email,
                 String phone,
                 UserStatus status,
                 User user,
                 TurfSport turfSport,
                 String specialization) {

        super(name, email, phone, status);

        this.coachId = coachId;
        this.user = user;
        this.turfSport = turfSport;
        this.specialization = specialization;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TurfSport getTurfSport() {
        return turfSport;
    }

    public void setTurfSport(TurfSport turfSport) {
        this.turfSport = turfSport;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
