package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(name = "notifications")
public class Notification {
 
    @Id
    @Column(name = "notification_id", nullable = false, unique = true)
    private String notificationId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    @Column(name = "title", nullable = false)
    private String title;
 
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
 
    @Column(name = "type", nullable = false)
    private String type;
 
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;
 
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
 
    public Notification() {
    }
 
    public Notification(String notificationId, User user, String title,
                         String message, String type, Boolean isRead,
                         LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
 
    public String getNotificationId() {
        return notificationId;
    }
 
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getType() {
        return type;
    }
 
    public void setType(String type) {
        this.type = type;
    }
 
    public Boolean getIsRead() {
        return isRead;
    }
 
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @PrePersist
    private void generateNotificationId() {
        if (notificationId == null || notificationId.isBlank()) {
            notificationId = EntityIdGenerator.generate("NOT");
        }
    }
}
