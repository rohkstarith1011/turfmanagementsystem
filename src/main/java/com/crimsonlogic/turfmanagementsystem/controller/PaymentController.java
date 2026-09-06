package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PaymentRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PaymentResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(
            IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // =========================
    // CREATE PAYMENT
    // =========================

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Valid @RequestBody PaymentRequestDTO requestDTO) {

        PaymentResponseDTO response =
                paymentService.createPayment(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>>
            getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments());
    }

    // =========================
    // GET PAYMENT BY BOOKING
    // =========================

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponseDTO>
            getPaymentByBooking(
                    @PathVariable String bookingId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByBooking(
                        bookingId));
    }

    // =========================
    // GET PAYMENTS BY STATUS
    // =========================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>>
            getPaymentsByStatus(
                    @PathVariable PaymentStatus status) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByStatus(
                        status));
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO>
            getPaymentById(
                    @PathVariable String paymentId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(
                        paymentId));
    }

    // =========================
    // MARK PAYMENT SUCCESS
    // =========================

    @PatchMapping("/{paymentId}/success")
    public ResponseEntity<PaymentResponseDTO>
            markPaymentSuccess(
                    @PathVariable String paymentId) {

        return ResponseEntity.ok(
                paymentService.markPaymentSuccess(
                        paymentId));
    }

    // =========================
    // MARK PAYMENT FAILED
    // =========================

    @PatchMapping("/{paymentId}/failed")
    public ResponseEntity<PaymentResponseDTO>
            markPaymentFailed(
                    @PathVariable String paymentId) {

        return ResponseEntity.ok(
                paymentService.markPaymentFailed(
                        paymentId));
    }

    // =========================
    // REFUND PAYMENT
    // =========================

    @PatchMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponseDTO>
            refundPayment(
                    @PathVariable String paymentId) {

        return ResponseEntity.ok(
                paymentService.refundPayment(
                        paymentId));
    }
}