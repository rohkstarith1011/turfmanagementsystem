package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(name = "teams")
public class Team {
 
    @Id
    @Column(name = "team_id", nullable = false, unique = true)
    private String teamId;
 
    @Column(name = "name", nullable = false)
    private String name;
 
    @Column(name = "description")
    private String description;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private Player createdBy;
 
    @Column(name = "status", nullable = false)
    private String status;
 
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
 
    public Team() {
    }
 
    public Team(String teamId, String name, String description, Sport sport,
                Player createdBy, String status, LocalDateTime createdAt) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        this.sport = sport;
        this.createdBy = createdBy;
        this.status = status;
        this.createdAt = createdAt;
    }
 
    public String getTeamId() {
        return teamId;
    }
 
    public void setTeamId(String teamId) {
        this.teamId = teamId;
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
 
    public Sport getSport() {
        return sport;
    }
 
    public void setSport(Sport sport) {
        this.sport = sport;
    }
 
    public Player getCreatedBy() {
        return createdBy;
    }
 
    public void setCreatedBy(Player createdBy) {
        this.createdBy = createdBy;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @PrePersist
    private void generateTeamId() {
        if (teamId == null || teamId.isBlank()) {
            teamId = EntityIdGenerator.generate("TEM");
        }
    }
}