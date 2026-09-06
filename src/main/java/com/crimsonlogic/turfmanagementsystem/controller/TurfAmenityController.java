package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfAmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfAmenityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfAmenityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/turf-amenities")
public class TurfAmenityController {

    private final ITurfAmenityService turfAmenityService;

    public TurfAmenityController(
            ITurfAmenityService turfAmenityService) {

        this.turfAmenityService = turfAmenityService;
    }

    @PostMapping
    public ResponseEntity<TurfAmenityResponseDTO> createTurfAmenity(
            @Valid @RequestBody TurfAmenityRequestDTO requestDTO) {

        TurfAmenityResponseDTO response =
                turfAmenityService.createTurfAmenity(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TurfAmenityResponseDTO>>
            getAllTurfAmenities() {

        return ResponseEntity.ok(
                turfAmenityService.getAllTurfAmenities());
    }

    @GetMapping("/{turfAmenityId}")
    public ResponseEntity<TurfAmenityResponseDTO>
            getTurfAmenityById(
                    @PathVariable String turfAmenityId) {

        return ResponseEntity.ok(
                turfAmenityService
                        .getTurfAmenityById(turfAmenityId));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<TurfAmenityResponseDTO>>
            getTurfAmenitiesByFacilityId(
                    @PathVariable String facilityId) {

        return ResponseEntity.ok(
                turfAmenityService
                        .getTurfAmenitiesByFacilityId(facilityId));
    }

    @GetMapping("/amenity/{amenityId}")
    public ResponseEntity<List<TurfAmenityResponseDTO>>
            getTurfAmenitiesByAmenityId(
                    @PathVariable String amenityId) {

        return ResponseEntity.ok(
                turfAmenityService
                        .getTurfAmenitiesByAmenityId(amenityId));
    }

    @PutMapping("/{turfAmenityId}")
    public ResponseEntity<TurfAmenityResponseDTO> updateTurfAmenity(
            @PathVariable String turfAmenityId,
            @Valid @RequestBody TurfAmenityRequestDTO requestDTO) {

        return ResponseEntity.ok(
                turfAmenityService.updateTurfAmenity(
                        turfAmenityId,
                        requestDTO));
    }

    @PatchMapping("/{turfAmenityId}/deactivate")
    public ResponseEntity<Void> deactivateTurfAmenity(
            @PathVariable String turfAmenityId) {

        turfAmenityService.deactivateTurfAmenity(
                turfAmenityId);

        return ResponseEntity.noContent().build();
    }
}