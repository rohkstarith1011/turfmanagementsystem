package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationPolicyRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CancellationPolicyResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICancellationPolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cancellation-policies")
public class CancellationPolicyController {

    private final ICancellationPolicyService cancellationPolicyService;

    public CancellationPolicyController(
            ICancellationPolicyService cancellationPolicyService) {

        this.cancellationPolicyService =
                cancellationPolicyService;
    }

    // ============================================================
    // CREATE POLICY
    // ============================================================

    @PostMapping
    public ResponseEntity<CancellationPolicyResponseDTO>
    createPolicy(
            @Valid @RequestBody
            CancellationPolicyRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cancellationPolicyService
                        .createPolicy(requestDTO));
    }

    // ============================================================
    // UPDATE POLICY
    // ============================================================

    @PutMapping("/{policyId}")
    public ResponseEntity<CancellationPolicyResponseDTO>
    updatePolicy(
            @PathVariable String policyId,
            @Valid @RequestBody
            CancellationPolicyRequestDTO requestDTO) {

        return ResponseEntity.ok(
                cancellationPolicyService
                        .updatePolicy(
                                policyId,
                                requestDTO));
    }

    // ============================================================
    // GET ALL POLICIES
    // ============================================================

    @GetMapping
    public ResponseEntity<List<CancellationPolicyResponseDTO>>
    getAllPolicies() {

        return ResponseEntity.ok(
                cancellationPolicyService
                        .getAllPolicies());
    }

    // ============================================================
    // GET POLICY BY ID
    // ============================================================

    @GetMapping("/{policyId}")
    public ResponseEntity<CancellationPolicyResponseDTO>
    getPolicyById(
            @PathVariable String policyId) {

        return ResponseEntity.ok(
                cancellationPolicyService
                        .getPolicyById(policyId));
    }

    // ============================================================
    // GET POLICY BY FACILITY
    // ============================================================

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<CancellationPolicyResponseDTO>
    getPolicyByFacility(
            @PathVariable String facilityId) {

        return ResponseEntity.ok(
                cancellationPolicyService
                        .getPolicyByFacility(
                                facilityId));
    }

    // ============================================================
    // DEACTIVATE POLICY
    // ============================================================

    @PatchMapping("/{policyId}/deactivate")
    public ResponseEntity<Void>
    deactivatePolicy(
            @PathVariable String policyId) {

        cancellationPolicyService
                .deactivatePolicy(policyId);

        return ResponseEntity.noContent().build();
    }
}