package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PaymentRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PaymentResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;

public interface IPaymentService {

    PaymentResponseDTO createPayment(
            PaymentRequestDTO requestDTO);

    PaymentResponseDTO getPaymentById(
            String paymentId);

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO getPaymentByBooking(
            String bookingId);

    List<PaymentResponseDTO> getPaymentsByStatus(
            PaymentStatus status);

    PaymentResponseDTO markPaymentSuccess(
            String paymentId);

    PaymentResponseDTO markPaymentFailed(
            String paymentId);

    PaymentResponseDTO refundPayment(
            String paymentId);
}