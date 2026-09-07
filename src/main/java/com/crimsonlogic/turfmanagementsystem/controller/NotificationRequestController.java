package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.NotificationRequestRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.NotificationRequestResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.INotificationRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notification-requests")
public class NotificationRequestController {

    private final INotificationRequestService notificationRequestService;

    public NotificationRequestController(
            INotificationRequestService notificationRequestService) {

        this.notificationRequestService =
                notificationRequestService;
    }

    // ============================================================
    // PLAYER INVITATION
    // ============================================================

    @PostMapping("/player-invitation")
    public ResponseEntity<NotificationRequestResponseDTO>
            createPlayerInvitation(
                    @Valid @RequestBody
                    NotificationRequestRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationRequestService
                        .createPlayerInvitation(requestDTO));
    }

    // ============================================================
    // GET ALL REQUESTS
    // ============================================================

    @GetMapping
    public ResponseEntity<List<NotificationRequestResponseDTO>>
            getAllRequests() {

        return ResponseEntity.ok(
                notificationRequestService.getAllRequests());
    }

    // ============================================================
    // GET REQUEST BY ID
    // ============================================================

    @GetMapping("/{notificationRequestId}")
    public ResponseEntity<NotificationRequestResponseDTO>
            getRequestById(
                    @PathVariable String notificationRequestId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .getRequestById(notificationRequestId));
    }

    // ============================================================
    // GET REQUESTS BY USER
    // ============================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationRequestResponseDTO>>
            getRequestsByUser(
                    @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .getRequestsByUser(userId));
    }

    // ============================================================
    // GET PENDING REQUESTS BY USER
    // ============================================================

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<NotificationRequestResponseDTO>>
            getPendingRequestsByUser(
                    @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .getPendingRequestsByUser(userId));
    }

    // ============================================================
    // ACCEPT PLAYER INVITATION
    // ============================================================

    @PatchMapping("/{notificationRequestId}/accept-player")
    public ResponseEntity<NotificationRequestResponseDTO>
            acceptPlayerInvitation(
                    @PathVariable String notificationRequestId,
                    @RequestParam String userId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .acceptPlayerInvitation(
                                notificationRequestId,
                                userId));
    }

    // ============================================================
    // REJECT PLAYER INVITATION
    // ============================================================

    @PatchMapping("/{notificationRequestId}/reject-player")
    public ResponseEntity<NotificationRequestResponseDTO>
            rejectPlayerInvitation(
                    @PathVariable String notificationRequestId,
                    @RequestParam String userId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .rejectPlayerInvitation(
                                notificationRequestId,
                                userId));
    }

    // ============================================================
    // CREATE COACH REQUESTS FOR BOOKING
    // ============================================================

    @PostMapping("/coach/{bookingId}")
    public ResponseEntity<List<NotificationRequestResponseDTO>>
            createCoachRequestsForBooking(
                    @PathVariable String bookingId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationRequestService
                        .createCoachRequestsForBooking(
                                bookingId));
    }

    // ============================================================
    // ACCEPT COACH REQUEST
    // ============================================================

    @PatchMapping("/{notificationRequestId}/accept-coach")
    public ResponseEntity<NotificationRequestResponseDTO>
            acceptCoachRequest(
                    @PathVariable String notificationRequestId,
                    @RequestParam String coachUserId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .acceptCoachRequest(
                                notificationRequestId,
                                coachUserId));
    }

    // ============================================================
    // REJECT COACH REQUEST
    // ============================================================

    @PatchMapping("/{notificationRequestId}/reject-coach")
    public ResponseEntity<NotificationRequestResponseDTO>
            rejectCoachRequest(
                    @PathVariable String notificationRequestId,
                    @RequestParam String coachUserId) {

        return ResponseEntity.ok(
                notificationRequestService
                        .rejectCoachRequest(
                                notificationRequestId,
                                coachUserId));
    }
}