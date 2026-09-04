package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



import java.time.LocalDateTime;

public class TeamPlayerResponseDTO {

    private String teamPlayerId;

    private String teamId;

    private String playerId;

    private LocalDateTime joinedAt;

    private String status;

    // Getters and Setters

    public String getTeamPlayerId() {
        return teamPlayerId;
    }

    public void setTeamPlayerId(String teamPlayerId) {
        this.teamPlayerId = teamPlayerId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
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
}