package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.BookingRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.BookingResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IBookingService;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CancellationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICancellationService;
import jakarta.validation.Valid;
import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.RescheduleBookingRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final IBookingService bookingService;
    private final ICancellationService cancellationService;
    public BookingController(IBookingService bookingService, ICancellationService cancellationService) {
        this.bookingService = bookingService;
        this.cancellationService =
                cancellationService;
    }

    // CREATE BOOKING
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO requestDTO) {

        BookingResponseDTO response =
                bookingService.createBooking(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL BOOKINGS
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    // GET BOOKING BY ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @PathVariable String bookingId) {

        return ResponseEntity.ok(
                bookingService.getBookingById(bookingId)
        );
    }

    // GET BOOKINGS BY PLAYER
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByPlayer(
            @PathVariable String playerId) {

        return ResponseEntity.ok(
                bookingService.getBookingsByPlayer(playerId)
        );
    }

    // GET BOOKINGS BY SLOT
    @GetMapping("/slot/{slotId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsBySlot(
            @PathVariable String slotId) {

        return ResponseEntity.ok(
                bookingService.getBookingsBySlot(slotId)
        );
    }

    // GET BOOKINGS BY DATE
    @GetMapping("/date/{date}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByDate(
            @PathVariable String date) {

        return ResponseEntity.ok(
                bookingService.getBookingsByDate(date)
        );
    }

    // GET BOOKINGS BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                bookingService.getBookingsByStatus(status)
        );
    }

    // UPDATE BOOKING
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> updateBooking(
            @PathVariable String bookingId,
            @Valid @RequestBody BookingRequestDTO requestDTO) {

        return ResponseEntity.ok(
                bookingService.updateBooking(
                        bookingId,
                        requestDTO
                )
        );
    }

    // CANCEL BOOKING
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<CancellationResponseDTO>
    cancelBooking(
            @PathVariable String bookingId,
            @Valid @RequestBody
            CancellationRequestDTO requestDTO) {

        requestDTO.setBookingId(bookingId);

        return ResponseEntity.ok(
                cancellationService
                        .cancelBooking(requestDTO));
    }
    @PostMapping("/{bookingId}/reschedule")
    public ResponseEntity<BookingResponseDTO> rescheduleBooking(
            @PathVariable String bookingId,
            @Valid @RequestBody RescheduleBookingRequestDTO requestDTO) {

        BookingResponseDTO response =
                bookingService.rescheduleBooking(
                        bookingId,
                        requestDTO.getNewSlotId());

        return ResponseEntity.ok(response);
    }
}