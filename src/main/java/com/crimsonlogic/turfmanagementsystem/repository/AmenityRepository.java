package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity, String> {

    Optional<Amenity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}