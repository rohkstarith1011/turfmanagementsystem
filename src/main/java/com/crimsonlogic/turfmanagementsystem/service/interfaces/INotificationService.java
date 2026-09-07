package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.NotificationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.NotificationResponseDTO;

public interface INotificationService {

    NotificationResponseDTO createNotification(
            NotificationRequestDTO requestDTO);

    List<NotificationResponseDTO> getAllNotifications();

    NotificationResponseDTO getNotificationById(
            String notificationId);

    List<NotificationResponseDTO> getNotificationsByUser(
            String userId);

    List<NotificationResponseDTO> getUnreadNotificationsByUser(
            String userId);

    NotificationResponseDTO markNotificationAsRead(
            String notificationId);
}