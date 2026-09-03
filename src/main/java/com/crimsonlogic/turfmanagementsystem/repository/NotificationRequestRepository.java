package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.NotificationRequest;

public interface NotificationRequestRepository
        extends JpaRepository<NotificationRequest, String> {

    List<NotificationRequest> findByUserUserId(String userId);
}