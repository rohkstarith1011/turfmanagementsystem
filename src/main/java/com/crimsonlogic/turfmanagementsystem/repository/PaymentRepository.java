package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Payment;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByBookingBookingId(String bookingId);

    boolean existsByBookingBookingId(String bookingId);

    List<Payment> findByStatus(PaymentStatus status);
}