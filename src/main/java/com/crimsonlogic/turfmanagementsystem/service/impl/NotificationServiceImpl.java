package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.NotificationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.NotificationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Notification;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.repository.NotificationRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.INotificationService;

@Service
public class NotificationServiceImpl
        implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // ============================================================
    // CREATE NOTIFICATION
    // ============================================================

    @Override
    @Transactional
    public NotificationResponseDTO createNotification(
            NotificationRequestDTO requestDTO) {

        User user = userRepository
                .findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(requestDTO.getTitle());
        notification.setMessage(requestDTO.getMessage());
        notification.setType(requestDTO.getType());

        // New notifications are always unread
        notification.setIsRead(false);

        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification =
                notificationRepository.save(notification);

        return mapToResponseDTO(savedNotification);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO>
            getAllNotifications() {

        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDTO getNotificationById(
            String notificationId) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found"));

        return mapToResponseDTO(notification);
    }

    // ============================================================
    // GET BY USER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO>
            getNotificationsByUser(String userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(
                    "User not found");
        }

        return notificationRepository
                .findByUserUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // GET UNREAD BY USER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO>
            getUnreadNotificationsByUser(String userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(
                    "User not found");
        }

        return notificationRepository
                .findByUserUserIdAndIsReadFalse(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // MARK AS READ
    // ============================================================

    @Override
    @Transactional
    public NotificationResponseDTO markNotificationAsRead(
            String notificationId) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found"));

        notification.setIsRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return mapToResponseDTO(updatedNotification);
    }

    // ============================================================
    // MAPPER
    // ============================================================

    private NotificationResponseDTO mapToResponseDTO(
            Notification notification) {

        NotificationResponseDTO responseDTO =
                new NotificationResponseDTO();

        responseDTO.setNotificationId(
                notification.getNotificationId());

        if (notification.getUser() != null) {
            responseDTO.setUserId(
                    notification.getUser().getUserId());
        }

        responseDTO.setTitle(
                notification.getTitle());

        responseDTO.setMessage(
                notification.getMessage());

        responseDTO.setType(
                notification.getType());

        responseDTO.setIsRead(
                notification.getIsRead());

        responseDTO.setCreatedAt(
                notification.getCreatedAt());

        return responseDTO;
    }
}