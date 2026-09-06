package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PaymentRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PaymentResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.Payment;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PaymentRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    // =========================
    // CREATE PAYMENT
    // =========================

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(
            PaymentRequestDTO requestDTO) {

        Booking booking = bookingRepository
                .findById(requestDTO.getBookingId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cannot create payment for a cancelled booking");
        }

        if (paymentRepository
                .existsByBookingBookingId(
                        booking.getBookingId())) {

            throw new IllegalArgumentException(
                    "Payment already exists for this booking");
        }

        if (requestDTO.getAmount() == null
                || requestDTO.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than 0");
        }

        if (booking.getTotalAmount() == null) {
            throw new IllegalArgumentException(
                    "Booking total amount is not available");
        }

        if (Double.compare(
                requestDTO.getAmount(),
                booking.getTotalAmount()) != 0) {

            throw new IllegalArgumentException(
                    "Payment amount must match booking total amount");
        }

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(requestDTO.getAmount());
        payment.setPaymentMethod(
                requestDTO.getPaymentMethod());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.INITIATED);

        Payment savedPayment =
                paymentRepository.save(payment);

        return mapToResponseDTO(savedPayment);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(
            String paymentId) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found"));

        return mapToResponseDTO(payment);
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // GET BY BOOKING
    // =========================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByBooking(
            String bookingId) {

        Payment payment =
                paymentRepository
                        .findByBookingBookingId(bookingId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found for this booking"));

        return mapToResponseDTO(payment);
    }

    // =========================
    // GET BY STATUS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByStatus(
            PaymentStatus status) {

        return paymentRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // MARK SUCCESS
    // =========================

    @Override
    @Transactional
    public PaymentResponseDTO markPaymentSuccess(
            String paymentId) {

        Payment payment = getPaymentEntity(paymentId);

        if (payment.getStatus() != PaymentStatus.INITIATED) {
            throw new IllegalArgumentException(
                    "Only INITIATED payments can be marked SUCCESS");
        }

        payment.setStatus(PaymentStatus.SUCCESS);

        Payment savedPayment =
                paymentRepository.save(payment);

        return mapToResponseDTO(savedPayment);
    }

    // =========================
    // MARK FAILED
    // =========================

    @Override
    @Transactional
    public PaymentResponseDTO markPaymentFailed(
            String paymentId) {

        Payment payment = getPaymentEntity(paymentId);

        if (payment.getStatus() != PaymentStatus.INITIATED) {
            throw new IllegalArgumentException(
                    "Only INITIATED payments can be marked FAILED");
        }

        payment.setStatus(PaymentStatus.FAILED);

        Payment savedPayment =
                paymentRepository.save(payment);

        return mapToResponseDTO(savedPayment);
    }

    // =========================
    // REFUND
    // =========================

    @Override
    @Transactional
    public PaymentResponseDTO refundPayment(
            String paymentId) {

        Payment payment = getPaymentEntity(paymentId);

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException(
                    "Only SUCCESS payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        Payment savedPayment =
                paymentRepository.save(payment);

        return mapToResponseDTO(savedPayment);
    }

    // =========================
    // GET ENTITY
    // =========================

    private Payment getPaymentEntity(
            String paymentId) {

        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found"));
    }

    // =========================
    // MAPPER
    // =========================

    private PaymentResponseDTO mapToResponseDTO(
            Payment payment) {

        PaymentResponseDTO response =
                new PaymentResponseDTO();

        response.setPaymentId(
                payment.getPaymentId());

        response.setBookingId(
                payment.getBooking()
                        .getBookingId());

        response.setAmount(
                payment.getAmount());

        response.setPaymentMethod(
                payment.getPaymentMethod());

        response.setPaymentDate(
                payment.getPaymentDate());

        response.setStatus(
                payment.getStatus());

        return response;
    }
}