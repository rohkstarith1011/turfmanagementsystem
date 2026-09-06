package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {

    Optional<Player> findByUserUserId(String userId);

    boolean existsByUserUserId(String userId);
    
    List<Player> findByNameContainingIgnoreCase(String name);
}