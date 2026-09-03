package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserUserId(String userId);

    boolean existsByUserUserIdAndRoleRoleId(String userId, String roleId);
}