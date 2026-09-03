package com.crimsonlogic.turfmanagementsystem.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Slot;

public interface SlotRepository extends JpaRepository<Slot, String> {

    List<Slot> findByPlayingAreaPlayingAreaId(String playingAreaId);

    List<Slot> findByPlayingAreaPlayingAreaIdAndSlotDate(
            String playingAreaId,
            LocalDate slotDate);

    List<Slot> findBySlotDate(LocalDate slotDate);

    List<Slot> findByPlayingAreaPlayingAreaIdAndSlotDateAndStartTimeLessThanAndEndTimeGreaterThan(
            String playingAreaId,
            LocalDate slotDate,
            LocalTime endTime,
            LocalTime startTime);
}