package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.CancellationPolicy;

public interface CancellationPolicyRepository
        extends JpaRepository<CancellationPolicy, String> {

    Optional<CancellationPolicy>
    findByFacilityFacilityId(String facilityId);

    boolean existsByFacilityFacilityId(String facilityId);
}