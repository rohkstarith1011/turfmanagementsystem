package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.TurfAmenity;

public interface TurfAmenityRepository extends JpaRepository<TurfAmenity, String> {

    List<TurfAmenity> findByFacilityFacilityId(String facilityId);

    List<TurfAmenity> findByAmenityAmenityId(String amenityId);

    boolean existsByFacilityFacilityIdAndAmenityAmenityId(
            String facilityId,
            String amenityId);
}