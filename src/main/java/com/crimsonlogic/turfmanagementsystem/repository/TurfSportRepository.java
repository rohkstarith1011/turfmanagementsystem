package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.TurfSport;

public interface TurfSportRepository extends JpaRepository<TurfSport, String> {

    List<TurfSport> findByFacilityFacilityId(String facilityId);

    List<TurfSport> findBySportSportId(String sportId);

    boolean existsByFacilityFacilityIdAndSportSportId(String facilityId, String sportId);
}