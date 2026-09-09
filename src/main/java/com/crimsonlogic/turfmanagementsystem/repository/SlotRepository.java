package com.crimsonlogic.turfmanagementsystem.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crimsonlogic.turfmanagementsystem.entity.Slot;

public interface SlotRepository extends JpaRepository<Slot, String> {

    List<Slot> findByPlayingAreaPlayingAreaId(String playingAreaId);

    List<Slot> findByPlayingAreaPlayingAreaIdAndSlotDate(
            String playingAreaId,
            LocalDate slotDate);

    List<Slot> findBySlotDate(LocalDate slotDate);

    @Query("""
            SELECT s FROM Slot s 
            WHERE s.playingArea.facility.facilityId = :facilityId 
            AND s.slotDate = :slotDate 
            AND s.status = 'AVAILABLE'
            """)
    List<Slot> findAvailableSlotsByFacilityAndDate(
            @Param("facilityId") String facilityId, 
            @Param("slotDate") LocalDate slotDate);

    List<Slot> findByPlayingAreaPlayingAreaIdAndSlotDateAndStartTimeLessThanAndEndTimeGreaterThan(
            String playingAreaId,
            LocalDate slotDate,
            LocalTime endTime,
            LocalTime startTime);
    
    @Query("""
            SELECT COUNT(s)
            FROM Slot s
            WHERE s.playingArea.facility.facilityId = :facilityId
            AND s.slotDate = :slotDate
            AND LOWER(s.status) = 'active'
            """)
    Long countActiveSlotsByFacilityAndDate(
            @Param("facilityId") String facilityId,
            @Param("slotDate") LocalDate slotDate);
    
    @Query("""
            SELECT COUNT(s)
            FROM Slot s
            WHERE LOWER(s.status) = 'active'
            """)
    Long countAllActiveSlots();
}