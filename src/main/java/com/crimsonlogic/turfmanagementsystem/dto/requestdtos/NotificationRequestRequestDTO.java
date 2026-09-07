package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NotificationRequestRequestDTO {

    @NotBlank(message = "Sender User ID is required")
    private String senderUserId;

    @NotBlank(message = "Receiver User ID is required")
    private String receiverUserId;

    @NotBlank(message = "Request type is required")
    @Size(max = 100, message = "Request type must not exceed 100 characters")
    private String requestType;

    @Size(max = 255, message = "Team ID must not exceed 255 characters")
    private String teamId;

    @Size(max = 255, message = "Booking ID must not exceed 255 characters")
    private String bookingId;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    public NotificationRequestRequestDTO() {
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}