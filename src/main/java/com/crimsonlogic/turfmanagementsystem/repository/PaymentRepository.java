package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crimsonlogic.turfmanagementsystem.entity.Payment;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByBookingBookingId(String bookingId);

    boolean existsByBookingBookingId(String bookingId);

    List<Payment> findByStatus(PaymentStatus status);
    
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.booking.slot.playingArea.facility.facilityId = :facilityId
            AND p.status = com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus.SUCCESS
            """)
    Double calculateRevenueByFacility(
            @Param("facilityId") String facilityId);
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus.SUCCESS
            """)
    Double calculateTotalRevenue();
}