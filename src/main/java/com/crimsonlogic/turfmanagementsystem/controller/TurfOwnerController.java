package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfOwnerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfOwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turf-owners")
public class TurfOwnerController {

    private final ITurfOwnerService turfOwnerService;

    public TurfOwnerController(ITurfOwnerService turfOwnerService) {
        this.turfOwnerService = turfOwnerService;
    }

    @PostMapping
    public ResponseEntity<TurfOwnerResponseDTO> createTurfOwner(
            @Valid @RequestBody TurfOwnerRequestDTO requestDTO) {

        TurfOwnerResponseDTO response =
                turfOwnerService.createTurfOwner(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TurfOwnerResponseDTO>> getAllTurfOwners() {

        List<TurfOwnerResponseDTO> response =
                turfOwnerService.getAllTurfOwners();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{turfOwnerId}")
    public ResponseEntity<TurfOwnerResponseDTO> getTurfOwnerById(
            @PathVariable String turfOwnerId) {

        TurfOwnerResponseDTO response =
                turfOwnerService.getTurfOwnerById(turfOwnerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<TurfOwnerResponseDTO> getTurfOwnerByUserId(
            @PathVariable String userId) {

        TurfOwnerResponseDTO response =
                turfOwnerService.getTurfOwnerByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{turfOwnerId}")
    public ResponseEntity<TurfOwnerResponseDTO> updateTurfOwner(
            @PathVariable String turfOwnerId,
            @Valid @RequestBody TurfOwnerRequestDTO requestDTO) {

        TurfOwnerResponseDTO response =
                turfOwnerService.updateTurfOwner(
                        turfOwnerId,
                        requestDTO
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{turfOwnerId}/deactivate")
    public ResponseEntity<Void> deactivateTurfOwner(
            @PathVariable String turfOwnerId) {

        turfOwnerService.deactivateTurfOwner(turfOwnerId);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<List<TurfOwnerResponseDTO>> getTurfOwnersByName(
            @PathVariable String name) {

        List<TurfOwnerResponseDTO> response =
                turfOwnerService.getTurfOwnersByName(name);

        return ResponseEntity.ok(response);
    }
}