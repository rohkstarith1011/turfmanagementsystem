package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Sport;

public interface SportRepository extends JpaRepository<Sport, String> {

    Optional<Sport> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}