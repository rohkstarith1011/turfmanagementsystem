package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AdminRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AdminResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(
            @Valid @RequestBody AdminRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminService.createAdmin(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {

        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> getAdminById(
            @PathVariable String adminId) {

        return ResponseEntity.ok(adminService.getAdminById(adminId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AdminResponseDTO> getAdminByUserId(
            @PathVariable String userId) {

        return ResponseEntity.ok(adminService.getAdminByUserId(userId));
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(
            @PathVariable String adminId,
            @Valid @RequestBody AdminRequestDTO requestDTO) {

        return ResponseEntity.ok(
                adminService.updateAdmin(adminId, requestDTO)
        );
    }

    @PatchMapping("/{adminId}/deactivate")
    public ResponseEntity<Void> deactivateAdmin(
            @PathVariable String adminId) {

        adminService.deactivateAdmin(adminId);

        return ResponseEntity.noContent().build();
    }
}