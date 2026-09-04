package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;

public class CancellationRequestDTO {

    @NotBlank(message = "Booking ID is required")
    private String bookingId;

    @NotBlank(message = "Cancellation reason is required")
    private String reason;

    // Getters and Setters

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}