package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Coach;

public interface CoachRepository extends JpaRepository<Coach, String> {

    Optional<Coach> findByUserUserId(String userId);

    boolean existsByUserUserId(String userId);

    List<Coach> findByTurfSportTurfSportId(String turfSportId);
}