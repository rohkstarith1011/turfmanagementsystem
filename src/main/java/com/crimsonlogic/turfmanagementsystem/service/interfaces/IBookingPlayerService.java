package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.BookingPlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.BookingPlayerResponseDTO;

public interface IBookingPlayerService {

    BookingPlayerResponseDTO createBookingPlayer(
            BookingPlayerRequestDTO requestDTO);

    BookingPlayerResponseDTO getBookingPlayerById(
            String bookingPlayerId);

    List<BookingPlayerResponseDTO> getAllBookingPlayers();

    List<BookingPlayerResponseDTO> getBookingPlayersByBooking(
            String bookingId);

    List<BookingPlayerResponseDTO> getBookingPlayersByPlayer(
            String playerId);

    BookingPlayerResponseDTO updateBookingPlayer(
            String bookingPlayerId,
            BookingPlayerRequestDTO requestDTO);

    void deactivateBookingPlayer(String bookingPlayerId);
}