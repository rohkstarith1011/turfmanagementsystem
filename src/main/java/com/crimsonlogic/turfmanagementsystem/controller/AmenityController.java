package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AmenityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAmenityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final IAmenityService amenityService;

    public AmenityController(IAmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @PostMapping
    public ResponseEntity<AmenityResponseDTO> createAmenity(
            @Valid @RequestBody AmenityRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(amenityService.createAmenity(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AmenityResponseDTO>> getAllAmenities() {

        return ResponseEntity.ok(amenityService.getAllAmenities());
    }

    @GetMapping("/{amenityId}")
    public ResponseEntity<AmenityResponseDTO> getAmenityById(
            @PathVariable String amenityId) {

        return ResponseEntity.ok(
                amenityService.getAmenityById(amenityId)
        );
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AmenityResponseDTO> getAmenityByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                amenityService.getAmenityByName(name)
        );
    }

    @PutMapping("/{amenityId}")
    public ResponseEntity<AmenityResponseDTO> updateAmenity(
            @PathVariable String amenityId,
            @Valid @RequestBody AmenityRequestDTO requestDTO) {

        return ResponseEntity.ok(
                amenityService.updateAmenity(amenityId, requestDTO)
        );
    }

    @PatchMapping("/{amenityId}/deactivate")
    public ResponseEntity<Void> deactivateAmenity(
            @PathVariable String amenityId) {

        amenityService.deactivateAmenity(amenityId);

        return ResponseEntity.noContent().build();
    }
}