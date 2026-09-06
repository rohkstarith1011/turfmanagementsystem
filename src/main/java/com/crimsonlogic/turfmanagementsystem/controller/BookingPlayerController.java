package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.BookingPlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.BookingPlayerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IBookingPlayerService;

@RestController
@RequestMapping("/api/booking-players")
public class BookingPlayerController {

    private final IBookingPlayerService bookingPlayerService;

    public BookingPlayerController(
            IBookingPlayerService bookingPlayerService) {
        this.bookingPlayerService = bookingPlayerService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public ResponseEntity<BookingPlayerResponseDTO> createBookingPlayer(
            @Valid @RequestBody BookingPlayerRequestDTO requestDTO) {

        BookingPlayerResponseDTO response =
                bookingPlayerService.createBookingPlayer(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ResponseEntity<List<BookingPlayerResponseDTO>>
            getAllBookingPlayers() {

        return ResponseEntity.ok(
                bookingPlayerService.getAllBookingPlayers());
    }

    // =========================
    // GET BY BOOKING
    // =========================

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookingPlayerResponseDTO>>
            getBookingPlayersByBooking(
                    @PathVariable String bookingId) {

        return ResponseEntity.ok(
                bookingPlayerService
                        .getBookingPlayersByBooking(bookingId));
    }

    // =========================
    // GET BY PLAYER
    // =========================

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<BookingPlayerResponseDTO>>
            getBookingPlayersByPlayer(
                    @PathVariable String playerId) {

        return ResponseEntity.ok(
                bookingPlayerService
                        .getBookingPlayersByPlayer(playerId));
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{bookingPlayerId}")
    public ResponseEntity<BookingPlayerResponseDTO>
            getBookingPlayerById(
                    @PathVariable String bookingPlayerId) {

        return ResponseEntity.ok(
                bookingPlayerService
                        .getBookingPlayerById(bookingPlayerId));
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{bookingPlayerId}")
    public ResponseEntity<BookingPlayerResponseDTO>
            updateBookingPlayer(
                    @PathVariable String bookingPlayerId,
                    @Valid @RequestBody BookingPlayerRequestDTO requestDTO) {

        return ResponseEntity.ok(
                bookingPlayerService.updateBookingPlayer(
                        bookingPlayerId,
                        requestDTO));
    }

    // =========================
    // DEACTIVATE
    // =========================

    @PatchMapping("/{bookingPlayerId}/deactivate")
    public ResponseEntity<Void> deactivateBookingPlayer(
            @PathVariable String bookingPlayerId) {

        bookingPlayerService.deactivateBookingPlayer(
                bookingPlayerId);

        return ResponseEntity.noContent().build();
    }
}