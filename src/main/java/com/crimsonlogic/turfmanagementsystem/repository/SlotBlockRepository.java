package com.crimsonlogic.turfmanagementsystem.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.SlotBlock;

public interface SlotBlockRepository extends JpaRepository<SlotBlock, String> {

    List<SlotBlock> findByPlayingAreaPlayingAreaId(String playingAreaId);

    List<SlotBlock> findByPlayingAreaPlayingAreaIdAndBlockDate(
            String playingAreaId,
            LocalDate blockDate);

    List<SlotBlock> findByBlockDate(LocalDate blockDate);

    List<SlotBlock> findByPlayingAreaPlayingAreaIdAndBlockDateAndStartTimeLessThanAndEndTimeGreaterThan(
            String playingAreaId,
            LocalDate blockDate,
            LocalTime endTime,
            LocalTime startTime);
}