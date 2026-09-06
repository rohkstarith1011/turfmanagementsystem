package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TeamRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TeamResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITeamService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(
            @Valid @RequestBody TeamRequestDTO requestDTO) {

        return new ResponseEntity<>(
                teamService.createTeam(requestDTO),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {

        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                teamService.getTeamsByName(name));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByPlayer(
            @PathVariable String playerId) {

        return ResponseEntity.ok(
                teamService.getTeamsByPlayer(playerId));
    }

    @GetMapping("/sport/{sportId}")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsBySport(
            @PathVariable String sportId) {

        return ResponseEntity.ok(
                teamService.getTeamsBySport(sportId));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDTO> getTeamById(
            @PathVariable String teamId) {

        return ResponseEntity.ok(
                teamService.getTeamById(teamId));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable String teamId,
            @Valid @RequestBody TeamRequestDTO requestDTO) {

        return ResponseEntity.ok(
                teamService.updateTeam(teamId, requestDTO));
    }

    @PatchMapping("/{teamId}/deactivate")
    public ResponseEntity<Void> deactivateTeam(
            @PathVariable String teamId) {

        teamService.deactivateTeam(teamId);

        return ResponseEntity.noContent().build();
    }
}