package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;

public class TeamPlayerRequestDTO {

    @NotBlank(message = "Team ID is required")
    private String teamId;

    @NotBlank(message = "Player ID is required")
    private String playerId;

    // Getters and Setters

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
}