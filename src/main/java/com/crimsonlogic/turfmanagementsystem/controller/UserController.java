package com.crimsonlogic.turfmanagementsystem.controller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO requestDTO) {

        UserResponseDTO responseDTO = userService.createUser(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable String userId) {

        UserResponseDTO responseDTO = userService.getUserById(userId);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequestDTO requestDTO) {

        UserResponseDTO responseDTO =
                userService.updateUser(userId, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable String userId) {

        userService.deactivateUser(userId);

        return ResponseEntity.noContent().build();
    }
}