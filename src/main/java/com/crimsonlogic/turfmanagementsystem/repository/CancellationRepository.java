package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Cancellation;

public interface CancellationRepository extends JpaRepository<Cancellation, String> {

    Optional<Cancellation> findByBookingBookingId(String bookingId);

    List<Cancellation> findByStatus(String status);
}