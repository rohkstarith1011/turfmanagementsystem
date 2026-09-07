package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.NotificationRequestRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.NotificationRequestResponseDTO;

public interface INotificationRequestService {

    NotificationRequestResponseDTO createPlayerInvitation(
            NotificationRequestRequestDTO requestDTO);

    List<NotificationRequestResponseDTO> getAllRequests();

    NotificationRequestResponseDTO getRequestById(
            String notificationRequestId);

    List<NotificationRequestResponseDTO> getRequestsByUser(
            String userId);

    List<NotificationRequestResponseDTO> getPendingRequestsByUser(
            String userId);

    NotificationRequestResponseDTO acceptPlayerInvitation(
            String notificationRequestId,
            String userId);

    NotificationRequestResponseDTO rejectPlayerInvitation(
            String notificationRequestId,
            String userId);

    List<NotificationRequestResponseDTO> createCoachRequestsForBooking(
            String bookingId);

    NotificationRequestResponseDTO acceptCoachRequest(
            String notificationRequestId,
            String coachUserId);

    NotificationRequestResponseDTO rejectCoachRequest(
            String notificationRequestId,
            String coachUserId);
}