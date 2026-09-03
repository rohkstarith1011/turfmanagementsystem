package com.crimsonlogic.turfmanagementsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;
import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;
 
@Entity
@Table(name = "payments")
public class Payment {
 
    @Id
    @Column(name = "payment_id", nullable = false, unique = true)
    private String paymentId;
 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;
 
    @Column(name = "amount", nullable = false)
    private Double amount;
 
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
 
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;
 
    public Payment() {
    }
 
    public Payment(String paymentId, Booking booking, Double amount,
                   String paymentMethod, LocalDateTime paymentDate,
                   PaymentStatus status) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.status = status;
    }
 
    public String getPaymentId() {
        return paymentId;
    }
 
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
 
    public Booking getBooking() {
        return booking;
    }
 
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
 
    public Double getAmount() {
        return amount;
    }
 
    public void setAmount(Double amount) {
        this.amount = amount;
    }
 
    public String getPaymentMethod() {
        return paymentMethod;
    }
 
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
 
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
 
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
 
    public PaymentStatus getStatus() {
        return status;
    }
 
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    @PrePersist
    private void generatePaymentId() {
        if (paymentId == null || paymentId.isBlank()) {
            paymentId = EntityIdGenerator.generate("PAY");
        }
    }
}
