package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByUserUserId(String userId);

    List<Notification> findByUserUserIdAndIsReadFalse(String userId);
}