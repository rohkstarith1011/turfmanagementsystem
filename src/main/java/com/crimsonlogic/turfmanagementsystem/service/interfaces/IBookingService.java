package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.BookingRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.BookingResponseDTO;

import java.util.List;

public interface IBookingService {

    BookingResponseDTO createBooking(BookingRequestDTO requestDTO);

    BookingResponseDTO getBookingById(String bookingId);

    List<BookingResponseDTO> getAllBookings();

    List<BookingResponseDTO> getBookingsByPlayer(String playerId);

    List<BookingResponseDTO> getBookingsBySlot(String slotId);

    List<BookingResponseDTO> getBookingsByDate(String date);

    List<BookingResponseDTO> getBookingsByStatus(String status);

    BookingResponseDTO updateBooking(
            String bookingId,
            BookingRequestDTO requestDTO);

    void cancelBooking(String bookingId);
    
    BookingResponseDTO rescheduleBooking(
            String bookingId,
            String newSlotId);
}