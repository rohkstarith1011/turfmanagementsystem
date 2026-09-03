package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.PlayingArea;

public interface PlayingAreaRepository extends JpaRepository<PlayingArea, String> {

    List<PlayingArea> findByFacilityFacilityId(String facilityId);

    List<PlayingArea> findByTurfSportTurfSportId(String turfSportId);
}