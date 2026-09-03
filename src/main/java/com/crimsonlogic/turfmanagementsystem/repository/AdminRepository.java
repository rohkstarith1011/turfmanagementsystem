package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByUserUserId(String userId);

    boolean existsByUserUserId(String userId);
}