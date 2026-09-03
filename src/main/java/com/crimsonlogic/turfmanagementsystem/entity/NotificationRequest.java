package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(name = "notification_requests")
public class NotificationRequest {
 
    @Id
    @Column(name = "notification_request_id", nullable = false, unique = true)
    private String notificationRequestId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    @Column(name = "request_type", nullable = false)
    private String requestType;
 
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
 
    @Column(name = "status", nullable = false)
    private String status;
 
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
 
    public NotificationRequest() {
    }
 
    public NotificationRequest(String notificationRequestId, User user,
                                String requestType, String message,
                                String status, LocalDateTime createdAt) {
        this.notificationRequestId = notificationRequestId;
        this.user = user;
        this.requestType = requestType;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }
 
    public String getNotificationRequestId() {
        return notificationRequestId;
    }
 
    public void setNotificationRequestId(String notificationRequestId) {
        this.notificationRequestId = notificationRequestId;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public String getRequestType() {
        return requestType;
    }
 
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @PrePersist
    private void generateNotificationRequestId() {
        if (notificationRequestId == null || notificationRequestId.isBlank()) {
            notificationRequestId = EntityIdGenerator.generate("NOTR");
        }
    }
}
