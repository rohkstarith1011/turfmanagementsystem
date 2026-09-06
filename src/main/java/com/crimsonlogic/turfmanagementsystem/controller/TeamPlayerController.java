package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TeamPlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TeamPlayerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITeamPlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/team-players")
public class TeamPlayerController {

    private final ITeamPlayerService teamPlayerService;

    public TeamPlayerController(
            ITeamPlayerService teamPlayerService) {
        this.teamPlayerService = teamPlayerService;
    }

    // Add player to team
    @PostMapping
    public ResponseEntity<TeamPlayerResponseDTO> addPlayerToTeam(
            @Valid @RequestBody TeamPlayerRequestDTO requestDTO) {

        TeamPlayerResponseDTO response =
                teamPlayerService.addPlayerToTeam(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all team-player memberships
    @GetMapping
    public ResponseEntity<List<TeamPlayerResponseDTO>> getAllTeamPlayers() {

        return ResponseEntity.ok(
                teamPlayerService.getAllTeamPlayers());
    }

    // Get membership by ID
    @GetMapping("/{teamPlayerId}")
    public ResponseEntity<TeamPlayerResponseDTO> getTeamPlayerById(
            @PathVariable String teamPlayerId) {

        return ResponseEntity.ok(
                teamPlayerService.getTeamPlayerById(teamPlayerId));
    }

    // Get all players belonging to a team
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamPlayerResponseDTO>> getPlayersByTeam(
            @PathVariable String teamId) {

        return ResponseEntity.ok(
                teamPlayerService.getPlayersByTeam(teamId));
    }

    // Get all teams belonging to a player
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<TeamPlayerResponseDTO>> getTeamsByPlayer(
            @PathVariable String playerId) {

        return ResponseEntity.ok(
                teamPlayerService.getTeamsByPlayer(playerId));
    }

    // Remove player from team
    @PatchMapping("/{teamPlayerId}/remove")
    public ResponseEntity<Void> removePlayerFromTeam(
            @PathVariable String teamPlayerId) {

        teamPlayerService.removePlayerFromTeam(teamPlayerId);

        return ResponseEntity.noContent().build();
    }
}