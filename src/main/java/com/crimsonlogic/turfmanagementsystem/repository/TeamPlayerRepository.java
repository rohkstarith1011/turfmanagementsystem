package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.TeamPlayer;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, String> {

    List<TeamPlayer> findByTeamTeamId(String teamId);

    List<TeamPlayer> findByPlayerPlayerId(String playerId);

    boolean existsByTeamTeamIdAndPlayerPlayerId(
            String teamId,
            String playerId);
}