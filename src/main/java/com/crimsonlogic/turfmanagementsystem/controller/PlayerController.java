package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PlayerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final IPlayerService playerService;

    public PlayerController(IPlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(
            @Valid @RequestBody PlayerRequestDTO requestDTO) {

        PlayerResponseDTO responseDTO = playerService.createPlayer(requestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> getAllPlayers() {

        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDTO> getPlayerById(
            @PathVariable String playerId) {

        return ResponseEntity.ok(playerService.getPlayerById(playerId));
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDTO> updatePlayer(
            @PathVariable String playerId,
            @Valid @RequestBody PlayerRequestDTO requestDTO) {

        return ResponseEntity.ok(
                playerService.updatePlayer(playerId, requestDTO));
    }

    @PatchMapping("/{playerId}/deactivate")
    public ResponseEntity<Void> deactivatePlayer(
            @PathVariable String playerId) {

        playerService.deactivatePlayer(playerId);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PlayerResponseDTO>> getPlayersByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                playerService.getPlayersByName(name));
    }
}