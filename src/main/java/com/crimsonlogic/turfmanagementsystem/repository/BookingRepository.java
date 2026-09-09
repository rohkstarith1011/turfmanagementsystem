package com.crimsonlogic.turfmanagementsystem.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.crimsonlogic.turfmanagementsystem.repository.projection.PopularLocationProjection;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.repository.projection.PopularSportProjection;
import jakarta.persistence.LockModeType;
import com.crimsonlogic.turfmanagementsystem.repository.projection.PeakBookingHourProjection;
import com.crimsonlogic.turfmanagementsystem.repository.projection.OwnerPerformanceProjection;
import com.crimsonlogic.turfmanagementsystem.repository.projection.FacilityPerformanceProjection;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByPlayerPlayerId(String playerId);

    List<Booking> findBySlotSlotId(String slotId);

    List<Booking> findByBookingDate(LocalDate bookingDate);

    List<Booking> findByStatus(BookingStatus status);

    
    
    boolean existsBySlotSlotIdAndStatusIn(
            String slotId,
            List<BookingStatus> statuses);
    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            AND b.bookingDate = :bookingDate
            """)
    Long countBookingsByFacilityAndDate(
            @Param("facilityId") String facilityId,
            @Param("bookingDate") LocalDate bookingDate);

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            AND b.bookingDate BETWEEN :startDate AND :endDate
            """)
    Long countBookingsByFacilityAndBookingDateBetween(
            @Param("facilityId") String facilityId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            AND (
                b.bookingDate > :currentDate
                OR (
                    b.bookingDate = :currentDate
                    AND b.startTime > :currentTime
                )
            )
            AND b.status IN (
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.PENDING,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CONFIRMED,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.RESCHEDULED
            )
            """)
    Long countUpcomingBookingsByFacility(
            @Param("facilityId") String facilityId,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime);


    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            """)
    Long countTotalBookingsByFacility(
            @Param("facilityId") String facilityId);


    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            AND b.status = com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            """)
    Long countCancelledBookingsByFacility(
            @Param("facilityId") String facilityId);


    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            AND b.status = com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.NO_SHOW
            """)
    Long countNoShowBookingsByFacility(
            @Param("facilityId") String facilityId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            """)
    List<Booking> findAllBookingsByFacility(
            @Param("facilityId") String facilityId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.bookingId = :bookingId")
    Optional<Booking> findByBookingIdForUpdate(
            @Param("bookingId") String bookingId);
    
    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.slot.playingArea.facility.facilityId = :facilityId
            AND b.bookingDate = :bookingDate
            AND b.status IN (
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.PENDING,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CONFIRMED,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.RESCHEDULED,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.COMPLETED
            )
            """)
    Long countOccupiedBookingsByFacilityAndDate(
            @Param("facilityId") String facilityId,
            @Param("bookingDate") LocalDate bookingDate);
    
    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status = com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            """)
    Long countCancelledBookings();
    @Query("""
            SELECT b.slot.playingArea.facility.location AS location,
                   COUNT(b) AS bookingCount
            FROM Booking b
            WHERE b.status <> com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            GROUP BY b.slot.playingArea.facility.location
            ORDER BY COUNT(b) DESC
            """)
    List<PopularLocationProjection> findPopularLocations();
    
    @Query("""
            SELECT b.slot.playingArea.turfSport.sport.name AS sportName,
                   COUNT(b) AS bookingCount
            FROM Booking b
            WHERE b.status <> com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            GROUP BY b.slot.playingArea.turfSport.sport.name
            ORDER BY COUNT(b) DESC
            """)
    List<PopularSportProjection> findPopularSports();
    
    @Query("""
            SELECT FUNCTION('DATE_FORMAT', b.startTime, '%H:00') AS bookingHour,
                   COUNT(b) AS bookingCount
            FROM Booking b
            WHERE b.status <> com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            GROUP BY FUNCTION('DATE_FORMAT', b.startTime, '%H:00')
            ORDER BY COUNT(b) DESC
            """)
    List<PeakBookingHourProjection> findPeakBookingHours();
    
    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status IN (
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.PENDING,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CONFIRMED,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.RESCHEDULED,
                com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.COMPLETED
            )
            """)
    Long countOccupiedBookings();
    
    @Query("""
            SELECT f.owner.name AS ownerName,
                   f.name AS facilityName,
                   COUNT(b) AS totalBookings,
                   COALESCE(SUM(
                       CASE
                           WHEN p.status = com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus.SUCCESS
                           THEN p.amount
                           ELSE 0
                       END
                   ), 0) AS revenue
            FROM Booking b
            JOIN b.slot s
            JOIN s.playingArea pa
            JOIN pa.facility f
            LEFT JOIN b.payment p
            WHERE b.status <> com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            GROUP BY f.owner.name, f.name
            ORDER BY COUNT(b) DESC
            """)
    List<OwnerPerformanceProjection> findOwnerPerformance();

    @Query("""
            SELECT f.name AS facilityName,
                   f.location AS location,
                   COUNT(b) AS totalBookings,
                   COALESCE(SUM(
                       CASE
                           WHEN p.status = com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus.SUCCESS
                           THEN p.amount
                           ELSE 0
                       END
                   ), 0) AS revenue,
                   COALESCE(AVG(r.rating), 0) AS rating
            FROM Booking b
            JOIN b.slot s
            JOIN s.playingArea pa
            JOIN pa.facility f
            LEFT JOIN b.payment p
            LEFT JOIN Review r ON r.booking.bookingId = b.bookingId
            WHERE b.status <> com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus.CANCELLED
            GROUP BY f.name, f.location
            ORDER BY COUNT(b) DESC
            """)
    List<FacilityPerformanceProjection> findFacilityPerformance();
}