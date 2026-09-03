package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.BookingPlayer;

public interface BookingPlayerRepository extends JpaRepository<BookingPlayer, String> {

    Optional<BookingPlayer> findByBookingBookingId(String bookingId);

    List<BookingPlayer> findByPlayerPlayerId(String playerId);
}