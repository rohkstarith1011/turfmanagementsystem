package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICoachService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final ICoachService coachService;

    public CoachController(ICoachService coachService) {
        this.coachService = coachService;
    }

    // Create a Coach profile
    @PostMapping
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachResponseDTO> createCoach(
            @Valid @RequestBody CoachRequestDTO requestDTO) {

        CoachResponseDTO response =
                coachService.createCoach(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all Coach profiles
    @GetMapping
    public ResponseEntity<List<CoachResponseDTO>> getAllCoaches() {

        return ResponseEntity.ok(
                coachService.getAllCoaches());
    }

    // Get Coach profile by ID
    @GetMapping("/{coachId}")
    public ResponseEntity<CoachResponseDTO> getCoachById(
            @PathVariable String coachId) {

        return ResponseEntity.ok(
                coachService.getCoachById(coachId));
    }

    // Get Coaches assigned to a TurfSport
    @GetMapping("/turf-sport/{turfSportId}")
    public ResponseEntity<List<CoachResponseDTO>> getCoachesByTurfSport(
            @PathVariable String turfSportId) {

        return ResponseEntity.ok(
                coachService.getCoachesByTurfSport(turfSportId));
    }

    // Update Coach profile
    @PutMapping("/{coachId}")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachResponseDTO> updateCoach(
            @PathVariable String coachId,
            @Valid @RequestBody CoachRequestDTO requestDTO) {

        return ResponseEntity.ok(
                coachService.updateCoach(coachId, requestDTO));
    }

    // Deactivate Coach profile
    @PatchMapping("/{coachId}/deactivate")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<Void> deactivateCoach(
            @PathVariable String coachId) {

        coachService.deactivateCoach(coachId);

        return ResponseEntity.noContent().build();
    }
}