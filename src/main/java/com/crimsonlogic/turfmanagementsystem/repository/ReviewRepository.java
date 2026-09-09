package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crimsonlogic.turfmanagementsystem.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {

    List<Review> findByFacilityFacilityId(String facilityId);

    List<Review> findByPlayerPlayerId(String playerId);

    Optional<Review> findByBookingBookingId(String bookingId);

    boolean existsByBookingBookingId(String bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.facility.facilityId = :facilityId")
    Double findAverageRatingByFacilityId(
            @Param("facilityId") String facilityId);
}