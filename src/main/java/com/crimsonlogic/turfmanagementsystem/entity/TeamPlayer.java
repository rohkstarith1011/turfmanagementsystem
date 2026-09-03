package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(
    name = "team_players",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team_id", "player_id"})
    }
)
public class TeamPlayer {
 
    @Id
    @Column(name = "team_player_id", nullable = false, unique = true)
    private String teamPlayerId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
 
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
 
    @Column(name = "status", nullable = false)
    private String status;
 
    public TeamPlayer() {
    }
 
    public TeamPlayer(String teamPlayerId, Team team, Player player,
                      LocalDateTime joinedAt, String status) {
        this.teamPlayerId = teamPlayerId;
        this.team = team;
        this.player = player;
        this.joinedAt = joinedAt;
        this.status = status;
    }
 
    public String getTeamPlayerId() {
        return teamPlayerId;
    }
 
    public void setTeamPlayerId(String teamPlayerId) {
        this.teamPlayerId = teamPlayerId;
    }
 
    public Team getTeam() {
        return team;
    }
 
    public void setTeam(Team team) {
        this.team = team;
    }
 
    public Player getPlayer() {
        return player;
    }
 
    public void setPlayer(Player player) {
        this.player = player;
    }
 
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
 
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    private void generateTeamPlayerId() {
        if (teamPlayerId == null || teamPlayerId.isBlank()) {
            teamPlayerId = EntityIdGenerator.generate("TEP");
        }
    }
}
