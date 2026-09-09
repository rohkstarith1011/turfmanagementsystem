package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachingClassRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachingClassResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICoachingClassService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coaching-classes")
public class CoachingClassController {

    private final ICoachingClassService coachingClassService;

    public CoachingClassController(
            ICoachingClassService coachingClassService) {
        this.coachingClassService = coachingClassService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachingClassResponseDTO> createCoachingClass(
            @Valid @RequestBody CoachingClassRequestDTO requestDTO) {

        CoachingClassResponseDTO response =
                coachingClassService.createCoachingClass(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CoachingClassResponseDTO>>
            getAllCoachingClasses() {

        return ResponseEntity.ok(
                coachingClassService.getAllCoachingClasses());
    }

    @GetMapping("/{coachingClassId}")
    public ResponseEntity<CoachingClassResponseDTO>
            getCoachingClassById(
                    @PathVariable String coachingClassId) {

        return ResponseEntity.ok(
                coachingClassService
                        .getCoachingClassById(coachingClassId));
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<CoachingClassResponseDTO>>
            getCoachingClassesByCoach(
                    @PathVariable String coachId) {

        return ResponseEntity.ok(
                coachingClassService
                        .getCoachingClassesByCoach(coachId));
    }

    @GetMapping("/turf-sport/{turfSportId}")
    public ResponseEntity<List<CoachingClassResponseDTO>>
            getCoachingClassesByTurfSport(
                    @PathVariable String turfSportId) {

        return ResponseEntity.ok(
                coachingClassService
                        .getCoachingClassesByTurfSport(turfSportId));
    }

    @PutMapping("/{coachingClassId}")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachingClassResponseDTO>
            updateCoachingClass(
                    @PathVariable String coachingClassId,
                    @Valid @RequestBody CoachingClassRequestDTO requestDTO) {

        return ResponseEntity.ok(
                coachingClassService.updateCoachingClass(
                        coachingClassId, requestDTO));
    }

    @PatchMapping("/{coachingClassId}/deactivate")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<Void> deactivateCoachingClass(
            @PathVariable String coachingClassId) {

        coachingClassService.deactivateCoachingClass(
                coachingClassId);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CoachingClassResponseDTO>>
            searchCoachingClassesByName(
                    @RequestParam String name) {

        return ResponseEntity.ok(
                coachingClassService
                        .searchCoachingClassesByName(name));
    }

    @GetMapping("/search/coach")
    public ResponseEntity<List<CoachingClassResponseDTO>>
            searchCoachingClassesByCoachName(
                    @RequestParam String name) {

        return ResponseEntity.ok(
                coachingClassService
                        .searchCoachingClassesByCoachName(name));
    }

    @GetMapping("/search/turf")
    public ResponseEntity<List<CoachingClassResponseDTO>>
            searchCoachingClassesByTurfName(
                    @RequestParam String name) {

        return ResponseEntity.ok(
                coachingClassService
                        .searchCoachingClassesByTurfName(name));
    }
}