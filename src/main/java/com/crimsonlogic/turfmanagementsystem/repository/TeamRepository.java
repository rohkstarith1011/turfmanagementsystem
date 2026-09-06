package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Team;

public interface TeamRepository extends JpaRepository<Team, String> {

    List<Team> findByCreatedByPlayerId(String playerId);

    List<Team> findBySportSportId(String sportId);

    List<Team> findByNameContainingIgnoreCase(String name);
}