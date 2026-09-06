package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfSportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfSportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfSportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/turf-sports")
public class TurfSportController {

    private final ITurfSportService turfSportService;

    public TurfSportController(ITurfSportService turfSportService) {
        this.turfSportService = turfSportService;
    }

    @PostMapping
    public ResponseEntity<TurfSportResponseDTO> createTurfSport(
            @Valid @RequestBody TurfSportRequestDTO requestDTO) {

        TurfSportResponseDTO response =
                turfSportService.createTurfSport(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TurfSportResponseDTO>> getAllTurfSports() {

        return ResponseEntity.ok(
                turfSportService.getAllTurfSports());
    }

    @GetMapping("/{turfSportId}")
    public ResponseEntity<TurfSportResponseDTO> getTurfSportById(
            @PathVariable String turfSportId) {

        return ResponseEntity.ok(
                turfSportService.getTurfSportById(turfSportId));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<TurfSportResponseDTO>> getTurfSportsByFacilityId(
            @PathVariable String facilityId) {

        return ResponseEntity.ok(
                turfSportService.getTurfSportsByFacilityId(facilityId));
    }

    @GetMapping("/sport/{sportId}")
    public ResponseEntity<List<TurfSportResponseDTO>> getTurfSportsBySportId(
            @PathVariable String sportId) {

        return ResponseEntity.ok(
                turfSportService.getTurfSportsBySportId(sportId));
    }

    @PutMapping("/{turfSportId}")
    public ResponseEntity<TurfSportResponseDTO> updateTurfSport(
            @PathVariable String turfSportId,
            @Valid @RequestBody TurfSportRequestDTO requestDTO) {

        return ResponseEntity.ok(
                turfSportService.updateTurfSport(
                        turfSportId,
                        requestDTO));
    }

    @PatchMapping("/{turfSportId}/deactivate")
    public ResponseEntity<Void> deactivateTurfSport(
            @PathVariable String turfSportId) {

        turfSportService.deactivateTurfSport(turfSportId);

        return ResponseEntity.noContent().build();
    }
}