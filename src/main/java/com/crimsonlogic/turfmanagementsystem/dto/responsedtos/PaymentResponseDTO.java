package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponseDTO {

    private String paymentId;

    private String bookingId;

    private Double amount;

    private String paymentMethod;

    private LocalDateTime paymentDate;

    private PaymentStatus status;

    // Getters and Setters

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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
}
