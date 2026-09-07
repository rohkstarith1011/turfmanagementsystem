package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.NotificationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.NotificationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.INotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(
            INotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    // ============================================================
    // CREATE NOTIFICATION
    // ============================================================

    @PostMapping
    public ResponseEntity<NotificationResponseDTO>
            createNotification(
                    @Valid @RequestBody
                    NotificationRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationService
                        .createNotification(requestDTO));
    }

    // ============================================================
    // GET ALL NOTIFICATIONS
    // ============================================================

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>>
            getAllNotifications() {

        return ResponseEntity.ok(
                notificationService
                        .getAllNotifications());
    }

    // ============================================================
    // GET NOTIFICATIONS BY USER
    // ============================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>>
            getNotificationsByUser(
                    @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByUser(userId));
    }

    // ============================================================
    // GET UNREAD NOTIFICATIONS BY USER
    // ============================================================

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>>
            getUnreadNotificationsByUser(
                    @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationService
                        .getUnreadNotificationsByUser(userId));
    }

    // ============================================================
    // GET NOTIFICATION BY ID
    // ============================================================

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDTO>
            getNotificationById(
                    @PathVariable String notificationId) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationById(
                                notificationId));
    }

    // ============================================================
    // MARK AS READ
    // ============================================================

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDTO>
            markNotificationAsRead(
                    @PathVariable String notificationId) {

        return ResponseEntity.ok(
                notificationService
                        .markNotificationAsRead(
                                notificationId));
    }
}