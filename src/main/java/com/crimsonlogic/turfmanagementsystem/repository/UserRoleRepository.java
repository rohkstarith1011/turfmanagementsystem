package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserUserId(String userId);
    
    List<UserRole> findByUserNameAndUserStatus(
            String name, UserStatus status);
}