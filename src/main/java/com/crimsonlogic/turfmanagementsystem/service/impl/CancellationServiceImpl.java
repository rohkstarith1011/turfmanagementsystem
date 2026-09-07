package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CancellationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.Cancellation;
import com.crimsonlogic.turfmanagementsystem.entity.CancellationPolicy;
import com.crimsonlogic.turfmanagementsystem.entity.Payment;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.CancellationPolicyRepository;
import com.crimsonlogic.turfmanagementsystem.repository.CancellationRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PaymentRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICancellationService;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPaymentService;

@Service
public class CancellationServiceImpl
        implements ICancellationService {

    private final BookingRepository bookingRepository;
    private final CancellationRepository cancellationRepository;
    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final PaymentRepository paymentRepository;
    private final IPaymentService paymentService;

    public CancellationServiceImpl(
            BookingRepository bookingRepository,
            CancellationRepository cancellationRepository,
            CancellationPolicyRepository cancellationPolicyRepository,
            PaymentRepository paymentRepository,
            IPaymentService paymentService) {

        this.bookingRepository = bookingRepository;
        this.cancellationRepository = cancellationRepository;
        this.cancellationPolicyRepository =
                cancellationPolicyRepository;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public CancellationResponseDTO cancelBooking(
            CancellationRequestDTO requestDTO) {

        // --------------------------------------------------------
        // 1. Find booking with pessimistic lock
        // --------------------------------------------------------

        Booking booking = bookingRepository
                .findByBookingIdForUpdate(
                        requestDTO.getBookingId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with ID: "
                                + requestDTO.getBookingId()));

        // --------------------------------------------------------
        // 2. Validate booking status
        // --------------------------------------------------------

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Completed booking cannot be cancelled");
        }

        // --------------------------------------------------------
        // 3. Prevent duplicate cancellation record
        // --------------------------------------------------------

        if (cancellationRepository
                .findByBookingBookingId(
                        booking.getBookingId())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Cancellation already exists for booking: "
                    + booking.getBookingId());
        }

        // --------------------------------------------------------
        // 4. Find facility through booking -> slot -> playing area
        // --------------------------------------------------------

        String facilityId = booking
                .getSlot()
                .getPlayingArea()
                .getFacility()
                .getFacilityId();

        // --------------------------------------------------------
        // 5. Find active cancellation policy
        // --------------------------------------------------------

        CancellationPolicy policy =
                cancellationPolicyRepository
                        .findByFacilityFacilityId(facilityId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Cancellation policy not found "
                                        + "for facility: "
                                        + facilityId));

        if (!"ACTIVE".equalsIgnoreCase(
                policy.getStatus())) {

            throw new IllegalArgumentException(
                    "Cancellation policy is not active "
                    + "for facility: " + facilityId);
        }

        // --------------------------------------------------------
        // 6. Calculate hours remaining
        // --------------------------------------------------------

        LocalDateTime bookingDateTime =
                LocalDateTime.of(
                        booking.getBookingDate(),
                        booking.getStartTime());

        LocalDateTime cancelledAt =
                LocalDateTime.now();

        long hoursRemaining = Duration.between(
                cancelledAt,
                bookingDateTime).toHours();

        // --------------------------------------------------------
        // 7. Calculate refund percentage
        // --------------------------------------------------------

        double refundPercentage;

        if (hoursRemaining > policy.getFullRefundHours()) {

            refundPercentage =
                    policy.getFullRefundPercentage();

        } else if (hoursRemaining >=
                policy.getPartialRefundHours()) {

            refundPercentage =
                    policy.getPartialRefundPercentage();

        } else if (hoursRemaining >=
                policy.getLimitedRefundHours()) {

            refundPercentage =
                    policy.getLimitedRefundPercentage();

        } else {

            refundPercentage =
                    policy.getMinimumRefundPercentage();
        }

        // --------------------------------------------------------
        // 8. Calculate refund amount
        // --------------------------------------------------------

        double refundAmount =
                booking.getTotalAmount()
                        * refundPercentage / 100.0;

        // Avoid floating point values such as 1499.999999
        refundAmount =
                Math.round(refundAmount * 100.0) / 100.0;

        // --------------------------------------------------------
        // 9. Refund payment if successful
        // --------------------------------------------------------

        Payment payment =
                paymentRepository
                        .findByBookingBookingId(
                                booking.getBookingId())
                        .orElse(null);

        if (payment != null
                && payment.getStatus()
                        == PaymentStatus.SUCCESS) {

            paymentService.refundPayment(
                    payment.getPaymentId());
        }

        // --------------------------------------------------------
        // 10. Update booking status
        // --------------------------------------------------------

        booking.setStatus(
                BookingStatus.CANCELLED);

        booking.setUpdatedAt(
                LocalDateTime.now());

        bookingRepository.save(booking);

        // --------------------------------------------------------
        // 11. Create cancellation record
        // --------------------------------------------------------

        Cancellation cancellation =
                new Cancellation();

        cancellation.setBooking(booking);
        cancellation.setReason(
                requestDTO.getReason());
        cancellation.setCancelledAt(
                cancelledAt);
        cancellation.setRefundAmount(
                refundAmount);
        cancellation.setStatus("COMPLETED");

        Cancellation savedCancellation =
                cancellationRepository.save(
                        cancellation);

        // --------------------------------------------------------
        // 12. Build response
        // --------------------------------------------------------

        return mapToResponse(savedCancellation);
    }

    // ============================================================
    // RESPONSE MAPPING
    // ============================================================

    private CancellationResponseDTO mapToResponse(
            Cancellation cancellation) {

        CancellationResponseDTO response =
                new CancellationResponseDTO();

        response.setCancellationId(
                cancellation.getCancellationId());

        response.setBookingId(
                cancellation.getBooking()
                        .getBookingId());

        response.setReason(
                cancellation.getReason());

        response.setCancelledAt(
                cancellation.getCancelledAt());

        response.setRefundAmount(
                cancellation.getRefundAmount());

        response.setStatus(
                cancellation.getStatus());

        return response;
    }
}