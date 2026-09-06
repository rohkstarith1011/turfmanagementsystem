package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final ISportService sportService;

    public SportController(ISportService sportService) {
        this.sportService = sportService;
    }

    @PostMapping
    public ResponseEntity<SportResponseDTO> createSport(
            @Valid @RequestBody SportRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sportService.createSport(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<SportResponseDTO>> getAllSports() {

        return ResponseEntity.ok(sportService.getAllSports());
    }

    @GetMapping("/{sportId}")
    public ResponseEntity<SportResponseDTO> getSportById(
            @PathVariable String sportId) {

        return ResponseEntity.ok(sportService.getSportById(sportId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SportResponseDTO> getSportByName(
            @PathVariable String name) {

        return ResponseEntity.ok(sportService.getSportByName(name));
    }

    @PutMapping("/{sportId}")
    public ResponseEntity<SportResponseDTO> updateSport(
            @PathVariable String sportId,
            @Valid @RequestBody SportRequestDTO requestDTO) {

        return ResponseEntity.ok(
                sportService.updateSport(sportId, requestDTO)
        );
    }

    @PatchMapping("/{sportId}/deactivate")
    public ResponseEntity<Void> deactivateSport(
            @PathVariable String sportId) {

        sportService.deactivateSport(sportId);

        return ResponseEntity.noContent().build();
    }
}