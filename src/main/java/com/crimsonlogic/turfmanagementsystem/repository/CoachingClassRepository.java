package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.CoachingClass;

public interface CoachingClassRepository extends JpaRepository<CoachingClass, String> {

    List<CoachingClass> findByCoachCoachId(String coachId);

    List<CoachingClass> findByTurfSportTurfSportId(String turfSportId);

    List<CoachingClass> findByNameContainingIgnoreCase(String name);

    List<CoachingClass> findByCoachNameContainingIgnoreCase(String name);

    List<CoachingClass> findByTurfSportFacilityNameContainingIgnoreCase(String name);
}