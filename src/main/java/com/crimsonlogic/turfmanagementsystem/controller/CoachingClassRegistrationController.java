package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachingClassRegistrationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachingClassRegistrationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICoachingClassRegistrationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaching-class-registrations")
public class CoachingClassRegistrationController {

    private final ICoachingClassRegistrationService coachingClassRegistrationService;

    public CoachingClassRegistrationController(
            ICoachingClassRegistrationService coachingClassRegistrationService) {
        this.coachingClassRegistrationService = coachingClassRegistrationService;
    }

    // CREATE REGISTRATION
    @PostMapping
    public ResponseEntity<CoachingClassRegistrationResponseDTO> createRegistration(
            @Valid @RequestBody CoachingClassRegistrationRequestDTO requestDTO) {

        CoachingClassRegistrationResponseDTO response =
                coachingClassRegistrationService.createRegistration(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET ALL REGISTRATIONS
    @GetMapping
    public ResponseEntity<List<CoachingClassRegistrationResponseDTO>> getAllRegistrations() {

        return ResponseEntity.ok(
                coachingClassRegistrationService.getAllRegistrations()
        );
    }

    // SEARCH BY COACHING CLASS NAME
    @GetMapping("/search/coaching-class")
    public ResponseEntity<List<CoachingClassRegistrationResponseDTO>> searchByCoachingClassName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                coachingClassRegistrationService
                        .searchRegistrationsByCoachingClassName(name)
        );
    }

    // SEARCH BY PLAYER NAME
    @GetMapping("/search/player")
    public ResponseEntity<List<CoachingClassRegistrationResponseDTO>> searchByPlayerName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                coachingClassRegistrationService
                        .searchRegistrationsByPlayerName(name)
        );
    }

    // GET REGISTRATIONS BY COACHING CLASS
    @GetMapping("/coaching-class/{coachingClassId}")
    public ResponseEntity<List<CoachingClassRegistrationResponseDTO>> getByCoachingClass(
            @PathVariable String coachingClassId) {

        return ResponseEntity.ok(
                coachingClassRegistrationService
                        .getRegistrationsByCoachingClass(coachingClassId)
        );
    }

    // GET REGISTRATIONS BY PLAYER
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<CoachingClassRegistrationResponseDTO>> getByPlayer(
            @PathVariable String playerId) {

        return ResponseEntity.ok(
                coachingClassRegistrationService
                        .getRegistrationsByPlayer(playerId)
        );
    }

    // GET REGISTRATION BY ID
    @GetMapping("/{registrationId}")
    public ResponseEntity<CoachingClassRegistrationResponseDTO> getRegistrationById(
            @PathVariable String registrationId) {

        return ResponseEntity.ok(
                coachingClassRegistrationService
                        .getRegistrationById(registrationId)
        );
    }

    // UPDATE REGISTRATION
    @PutMapping("/{registrationId}")
    public ResponseEntity<CoachingClassRegistrationResponseDTO> updateRegistration(
            @PathVariable String registrationId,
            @Valid @RequestBody CoachingClassRegistrationRequestDTO requestDTO) {

        return ResponseEntity.ok(
                coachingClassRegistrationService
                        .updateRegistration(registrationId, requestDTO)
        );
    }

    // DEACTIVATE REGISTRATION
    @PatchMapping("/{registrationId}/deactivate")
    public ResponseEntity<Void> deactivateRegistration(
            @PathVariable String registrationId) {

        coachingClassRegistrationService
                .deactivateRegistration(registrationId);

        return ResponseEntity.noContent().build();
    }
}