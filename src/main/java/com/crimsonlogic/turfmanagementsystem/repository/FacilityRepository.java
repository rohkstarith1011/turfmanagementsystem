package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, String> {

    List<Facility> findByCityIgnoreCase(String city);

    List<Facility> findByStateIgnoreCase(String state);

    List<Facility> findByLocalityIgnoreCase(String locality);

    List<Facility> findByTurfTypeIgnoreCase(String turfType);
}