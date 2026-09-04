package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRoleRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserRoleResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserRoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final IUserRoleService userRoleService;

    public UserRoleController(IUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public ResponseEntity<UserRoleResponseDTO> assignRole(
            @Valid @RequestBody UserRoleRequestDTO requestDTO) {

        UserRoleResponseDTO response =
                userRoleService.assignRole(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserRoleResponseDTO>> getAllUserRoles() {

        return ResponseEntity.ok(
                userRoleService.getAllUserRoles());
    }

    @GetMapping("/{userRoleId}")
    public ResponseEntity<UserRoleResponseDTO> getUserRoleById(
            @PathVariable String userRoleId) {

        return ResponseEntity.ok(
                userRoleService.getUserRoleById(userRoleId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getRolesByUserId(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                userRoleService.getRolesByUserId(userId));
    }

    @DeleteMapping("/{userRoleId}")
    public ResponseEntity<Void> removeRole(
            @PathVariable String userRoleId) {

        userRoleService.removeRole(userRoleId);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user/name/{name}")
    public ResponseEntity<List<UserRoleResponseDTO>> getRolesByUserName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                userRoleService.getRolesByUserName(name));
    }
}