package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(name = "audit_logs")
public class AuditLog {
 
    @Id
    @Column(name = "audit_log_id", nullable = false, unique = true)
    private String auditLogId;
 
    @Column(name = "action", nullable = false)
    private String action;
 
    @Column(name = "entity_type", nullable = false)
    private String entityType;
 
    @Column(name = "entity_id", nullable = false)
    private String entityId;
 
    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;
 
    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;
 
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
 
    public AuditLog() {
    }
 
    public AuditLog(String auditLogId, String action, String entityType,
                    String entityId, String oldValue, String newValue,
                    User performedBy, LocalDateTime timestamp) {
        this.auditLogId = auditLogId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.performedBy = performedBy;
        this.timestamp = timestamp;
    }
 
    public String getAuditLogId() {
        return auditLogId;
    }
 
    public void setAuditLogId(String auditLogId) {
        this.auditLogId = auditLogId;
    }
 
    public String getAction() {
        return action;
    }
 
    public void setAction(String action) {
        this.action = action;
    }
 
    public String getEntityType() {
        return entityType;
    }
 
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
 
    public String getEntityId() {
        return entityId;
    }
 
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
 
    public String getOldValue() {
        return oldValue;
    }
 
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
 
    public String getNewValue() {
        return newValue;
    }
 
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
 
    public User getPerformedBy() {
        return performedBy;
    }
 
    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }
 
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
 
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    @PrePersist
    private void generateAuditLogId() {
        if (auditLogId == null || auditLogId.isBlank()) {
            auditLogId = EntityIdGenerator.generate("AUD");
        }
    }
}
