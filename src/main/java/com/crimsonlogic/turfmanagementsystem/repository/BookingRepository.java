package com.crimsonlogic.turfmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByPlayerPlayerId(String playerId);

    List<Booking> findBySlotSlotId(String slotId);

    List<Booking> findByBookingDate(LocalDate bookingDate);

    List<Booking> findByStatus(BookingStatus status);

    boolean existsBySlotSlotIdAndStatusIn(
            String slotId,
            List<BookingStatus> statuses);
}