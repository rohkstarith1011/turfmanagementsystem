package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfManagerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfManagerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfManagerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turf-managers")
public class TurfManagerController {

    private final ITurfManagerService turfManagerService;

    public TurfManagerController(ITurfManagerService turfManagerService) {
        this.turfManagerService = turfManagerService;
    }

    @PostMapping
    public ResponseEntity<TurfManagerResponseDTO> createTurfManager(
            @Valid @RequestBody TurfManagerRequestDTO requestDTO) {

        TurfManagerResponseDTO response =
                turfManagerService.createTurfManager(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TurfManagerResponseDTO>> getAllTurfManagers() {

        return ResponseEntity.ok(
                turfManagerService.getAllTurfManagers());
    }

    @GetMapping("/{turfManagerId}")
    public ResponseEntity<TurfManagerResponseDTO> getTurfManagerById(
            @PathVariable String turfManagerId) {

        return ResponseEntity.ok(
                turfManagerService.getTurfManagerById(turfManagerId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<TurfManagerResponseDTO> getTurfManagerByUserId(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                turfManagerService.getTurfManagerByUserId(userId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<TurfManagerResponseDTO>> getTurfManagersByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                turfManagerService.getTurfManagersByName(name));
    }

    @PutMapping("/{turfManagerId}")
    public ResponseEntity<TurfManagerResponseDTO> updateTurfManager(
            @PathVariable String turfManagerId,
            @Valid @RequestBody TurfManagerRequestDTO requestDTO) {

        return ResponseEntity.ok(
                turfManagerService.updateTurfManager(
                        turfManagerId,
                        requestDTO));
    }

    @PatchMapping("/{turfManagerId}/deactivate")
    public ResponseEntity<Void> deactivateTurfManager(
            @PathVariable String turfManagerId) {

        turfManagerService.deactivateTurfManager(turfManagerId);

        return ResponseEntity.noContent().build();
    }
}