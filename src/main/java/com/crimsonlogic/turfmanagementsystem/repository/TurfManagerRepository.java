package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.TurfManager;

public interface TurfManagerRepository extends JpaRepository<TurfManager, String> {

    Optional<TurfManager> findByUserUserId(String userId);

    boolean existsByUserUserId(String userId);
}