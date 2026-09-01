package com.crimsonlogic.turfmanagementsystem.entity;



import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class Player extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String locality;

    @Column(nullable = false)
    private String skillLevel;

    @Column(nullable = false)
    private String preferredSports;

    public Player() {
        super();
    }

    public Player(Long playerId,
                  String name,
                  String email,
                  String phone,
                  UserStatus status,
                  User user,
                  String locality,
                  String skillLevel,
                  String preferredSports) {

        super(name, email, phone, status);

        this.playerId = playerId;
        this.user = user;
        this.locality = locality;
        this.skillLevel = skillLevel;
        this.preferredSports = preferredSports;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getPreferredSports() {
        return preferredSports;
    }

    public void setPreferredSports(String preferredSports) {
        this.preferredSports = preferredSports;
    }
}
