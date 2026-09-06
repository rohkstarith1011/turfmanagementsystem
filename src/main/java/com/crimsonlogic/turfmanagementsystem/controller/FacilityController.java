package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.FacilityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IFacilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final IFacilityService facilityService;

    public FacilityController(IFacilityService facilityService) {
        this.facilityService = facilityService;
    }

    // ---------------------------------------------------------
    // CREATE FACILITY
    // ---------------------------------------------------------

    @PostMapping
    public ResponseEntity<FacilityResponseDTO> createFacility(
            @Valid @RequestBody FacilityRequestDTO requestDTO) {

        FacilityResponseDTO response =
                facilityService.createFacility(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ---------------------------------------------------------
    // GET ALL FACILITIES
    // ---------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<FacilityResponseDTO>> getAllFacilities() {

        return ResponseEntity.ok(
                facilityService.getAllFacilities()
        );
    }

    // ---------------------------------------------------------
    // GET FACILITY BY ID
    // ---------------------------------------------------------

    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityResponseDTO> getFacilityById(
            @PathVariable String facilityId) {

        return ResponseEntity.ok(
                facilityService.getFacilityById(facilityId)
        );
    }

    // ---------------------------------------------------------
    // GET FACILITIES BY NAME
    // ---------------------------------------------------------

    @GetMapping("/name/{name}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                facilityService.getFacilitiesByName(name)
        );
    }

    // ---------------------------------------------------------
    // GET FACILITIES BY CITY
    // ---------------------------------------------------------

    @GetMapping("/city/{city}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                facilityService.getFacilitiesByCity(city)
        );
    }

    // ---------------------------------------------------------
    // GET FACILITIES BY STATE
    // ---------------------------------------------------------

    @GetMapping("/state/{state}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByState(
            @PathVariable String state) {

        return ResponseEntity.ok(
                facilityService.getFacilitiesByState(state)
        );
    }

    // ---------------------------------------------------------
    // GET FACILITIES BY LOCALITY
    // ---------------------------------------------------------

    @GetMapping("/locality/{locality}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByLocality(
            @PathVariable String locality) {

        return ResponseEntity.ok(
                facilityService.getFacilitiesByLocality(locality)
        );
    }

    // ---------------------------------------------------------
    // GET FACILITIES BY TURF TYPE
    // ---------------------------------------------------------

   

    // ---------------------------------------------------------
    // GET FACILITIES BY OWNER
    // ---------------------------------------------------------

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByOwnerId(
            @PathVariable String ownerId) {

        return ResponseEntity.ok(
                facilityService.getFacilitiesByOwnerId(ownerId)
        );
    }

    // ---------------------------------------------------------
    // GET FACILITIES BY MANAGER
    // ---------------------------------------------------------

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByManagerId(
            @PathVariable String managerId) {

        return ResponseEntity.ok(
                facilityService.getFacilitiesByManagerId(managerId)
        );
    }

    // ---------------------------------------------------------
    // UPDATE FACILITY
    // ---------------------------------------------------------

    @PutMapping("/{facilityId}")
    public ResponseEntity<FacilityResponseDTO> updateFacility(
            @PathVariable String facilityId,
            @Valid @RequestBody FacilityRequestDTO requestDTO) {

        return ResponseEntity.ok(
                facilityService.updateFacility(
                        facilityId,
                        requestDTO
                )
        );
    }

    // ---------------------------------------------------------
    // DEACTIVATE FACILITY
    // ---------------------------------------------------------

    @PatchMapping("/{facilityId}/deactivate")
    public ResponseEntity<Void> deactivateFacility(
            @PathVariable String facilityId) {

        facilityService.deactivateFacility(facilityId);

        return ResponseEntity.noContent().build();
    }
}