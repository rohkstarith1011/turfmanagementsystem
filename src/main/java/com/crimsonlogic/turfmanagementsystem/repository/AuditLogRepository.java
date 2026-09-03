package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    List<AuditLog> findByPerformedByUserId(String userId);

    List<AuditLog> findByEntityTypeAndEntityId(
            String entityType,
            String entityId);
}