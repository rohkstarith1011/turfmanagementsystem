package com.crimsonlogic.turfmanagementsystem.entity;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_requests")
public class NotificationRequest {

    @Id
    @Column(name = "notification_request_id", nullable = false, unique = true)
    private String notificationRequestId;

    /*
     * User who initiated the request.
     * Example:
     * Team creator -> invited Player
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender;

    /*
     * User who receives the request.
     * Existing database column user_id is retained for this.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User receiver;

    /*
     * Used for PLAYER_INVITATION.
     * Null for COACH_REQUEST.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    /*
     * Used for COACH_REQUEST.
     * Null for PLAYER_INVITATION.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

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

    public NotificationRequest(
            String notificationRequestId,
            User sender,
            User receiver,
            Team team,
            Booking booking,
            String requestType,
            String message,
            String status,
            LocalDateTime createdAt) {

        this.notificationRequestId = notificationRequestId;
        this.sender = sender;
        this.receiver = receiver;
        this.team = team;
        this.booking = booking;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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

        if (notificationRequestId == null
                || notificationRequestId.isBlank()) {

            notificationRequestId =
                    EntityIdGenerator.generate("NOTR");
        }
    }
}