package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.TurfOwner;

public interface TurfOwnerRepository extends JpaRepository<TurfOwner, String> {

    Optional<TurfOwner> findByUserUserId(String userId);

    boolean existsByUserUserId(String userId);
}