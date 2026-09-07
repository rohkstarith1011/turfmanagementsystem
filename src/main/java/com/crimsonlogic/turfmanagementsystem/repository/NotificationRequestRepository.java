package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.NotificationRequest;

public interface NotificationRequestRepository
        extends JpaRepository<NotificationRequest, String> {

    /*
     * All requests received by a user.
     */
    List<NotificationRequest> findByReceiverUserId(String userId);

    /*
     * Requests received by a user with a particular status.
     */
    List<NotificationRequest> findByReceiverUserIdAndStatus(
            String userId,
            String status);

    /*
     * Used when accepting/rejecting a request.
     * Ensures the receiver can only act on their own request.
     */
    Optional<NotificationRequest>
    findByNotificationRequestIdAndReceiverUserId(
            String notificationRequestId,
            String userId);

    /*
     * All coach requests belonging to a booking.
     */
    List<NotificationRequest> findByBookingBookingId(
            String bookingId);

    /*
     * Coach requests for a booking with a particular status.
     */
    List<NotificationRequest> findByBookingBookingIdAndStatus(
            String bookingId,
            String status);

    /*
     * Used to determine whether a particular coach has already
     * received a request for this booking.
     */
    boolean existsByBookingBookingIdAndReceiverUserId(
            String bookingId,
            String userId);
}