package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(name = "cancellations")
public class Cancellation {
 
    @Id
    @Column(name = "cancellation_id", nullable = false, unique = true)
    private String cancellationId;
 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;
 
    @Column(name = "reason", nullable = false)
    private String reason;
 
    @Column(name = "cancelled_at", nullable = false)
    private LocalDateTime cancelledAt;
 
    @Column(name = "refund_amount", nullable = false)
    private Double refundAmount;
 
    @Column(name = "status", nullable = false)
    private String status;
 
    public Cancellation() {
    }
 
    public Cancellation(String cancellationId, Booking booking, String reason,
                        LocalDateTime cancelledAt, Double refundAmount,
                        String status) {
        this.cancellationId = cancellationId;
        this.booking = booking;
        this.reason = reason;
        this.cancelledAt = cancelledAt;
        this.refundAmount = refundAmount;
        this.status = status;
    }
 
    public String getCancellationId() {
        return cancellationId;
    }
 
    public void setCancellationId(String cancellationId) {
        this.cancellationId = cancellationId;
    }
 
    public Booking getBooking() {
        return booking;
    }
 
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
 
    public String getReason() {
        return reason;
    }
 
    public void setReason(String reason) {
        this.reason = reason;
    }
 
    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
 
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
 
    public Double getRefundAmount() {
        return refundAmount;
    }
 
    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    private void generateCancellationId() {
        if (cancellationId == null || cancellationId.isBlank()) {
            cancellationId = EntityIdGenerator.generate("CAL");
        }
    }
}
