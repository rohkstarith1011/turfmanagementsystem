package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PlayingAreaRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PlayingAreaResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPlayingAreaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/playing-areas")
public class PlayingAreaController {

    private final IPlayingAreaService playingAreaService;

    public PlayingAreaController(
            IPlayingAreaService playingAreaService) {

        this.playingAreaService = playingAreaService;
    }

    @PostMapping
    public ResponseEntity<PlayingAreaResponseDTO>
            createPlayingArea(
                    @Valid @RequestBody
                    PlayingAreaRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(playingAreaService
                        .createPlayingArea(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PlayingAreaResponseDTO>>
            getAllPlayingAreas() {

        return ResponseEntity.ok(
                playingAreaService.getAllPlayingAreas());
    }

    @GetMapping("/{playingAreaId}")
    public ResponseEntity<PlayingAreaResponseDTO>
            getPlayingAreaById(
                    @PathVariable String playingAreaId) {

        return ResponseEntity.ok(
                playingAreaService
                        .getPlayingAreaById(playingAreaId));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<PlayingAreaResponseDTO>>
            getPlayingAreasByFacilityId(
                    @PathVariable String facilityId) {

        return ResponseEntity.ok(
                playingAreaService
                        .getPlayingAreasByFacilityId(facilityId));
    }

    @GetMapping("/turf-sport/{turfSportId}")
    public ResponseEntity<List<PlayingAreaResponseDTO>>
            getPlayingAreasByTurfSportId(
                    @PathVariable String turfSportId) {

        return ResponseEntity.ok(
                playingAreaService
                        .getPlayingAreasByTurfSportId(turfSportId));
    }

    @PutMapping("/{playingAreaId}")
    public ResponseEntity<PlayingAreaResponseDTO>
            updatePlayingArea(
                    @PathVariable String playingAreaId,
                    @Valid @RequestBody
                    PlayingAreaRequestDTO requestDTO) {

        return ResponseEntity.ok(
                playingAreaService.updatePlayingArea(
                        playingAreaId,
                        requestDTO));
    }

    @PatchMapping("/{playingAreaId}/deactivate")
    public ResponseEntity<Void>
            deactivatePlayingArea(
                    @PathVariable String playingAreaId) {

        playingAreaService
                .deactivatePlayingArea(playingAreaId);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PlayingAreaResponseDTO>> searchByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                playingAreaService.searchByName(name)
        );
    }
}