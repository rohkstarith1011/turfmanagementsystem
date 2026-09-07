package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.NotificationRequestRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.NotificationRequestResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.Coach;
import com.crimsonlogic.turfmanagementsystem.entity.Notification;
import com.crimsonlogic.turfmanagementsystem.entity.NotificationRequest;
import com.crimsonlogic.turfmanagementsystem.entity.Payment;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Team;
import com.crimsonlogic.turfmanagementsystem.entity.TeamPlayer;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.entity.enums.PaymentStatus;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.CoachRepository;
import com.crimsonlogic.turfmanagementsystem.repository.NotificationRepository;
import com.crimsonlogic.turfmanagementsystem.repository.NotificationRequestRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PaymentRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TeamPlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TeamRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.INotificationRequestService;

@Service
@Transactional
public class NotificationRequestServiceImpl
        implements INotificationRequestService {

    private final NotificationRequestRepository notificationRequestRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final BookingRepository bookingRepository;
    private final CoachRepository coachRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;

    public NotificationRequestServiceImpl(
            NotificationRequestRepository notificationRequestRepository,
            UserRepository userRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            TeamPlayerRepository teamPlayerRepository,
            BookingRepository bookingRepository,
            CoachRepository coachRepository,
            PaymentRepository paymentRepository,
            NotificationRepository notificationRepository) {

        this.notificationRequestRepository =
                notificationRequestRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.bookingRepository = bookingRepository;
        this.coachRepository = coachRepository;
        this.paymentRepository = paymentRepository;
        this.notificationRepository = notificationRepository;
    }

    // ============================================================
    // PLAYER INVITATION
    // ============================================================

    @Override
    public NotificationRequestResponseDTO createPlayerInvitation(
            NotificationRequestRequestDTO requestDTO) {

        if (!"PLAYER_INVITATION".equalsIgnoreCase(
                requestDTO.getRequestType())) {

            throw new IllegalArgumentException(
                    "Request type must be PLAYER_INVITATION");
        }

        if (requestDTO.getTeamId() == null
                || requestDTO.getTeamId().isBlank()) {

            throw new IllegalArgumentException(
                    "Team ID is required for player invitation");
        }

        if (requestDTO.getBookingId() != null
                && !requestDTO.getBookingId().isBlank()) {

            throw new IllegalArgumentException(
                    "Booking ID must not be provided for player invitation");
        }

        User sender = getUser(requestDTO.getSenderUserId());

        User receiver = getUser(requestDTO.getReceiverUserId());

        Team team = teamRepository.findById(requestDTO.getTeamId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Team not found"));

        if (!"ACTIVE".equalsIgnoreCase(team.getStatus())) {

            throw new IllegalArgumentException(
                    "Cannot invite a player to an inactive Team");
        }

        /*
         * Sender must be the Team creator.
         */
        Player senderPlayer =
                playerRepository.findByUserUserId(sender.getUserId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Sender does not have a Player profile"));

        if (!team.getCreatedBy()
                .getPlayerId()
                .equals(senderPlayer.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Only the Team creator can invite players");
        }

        /*
         * Receiver must have an active Player profile.
         */
        Player receiverPlayer =
                playerRepository.findByUserUserId(receiver.getUserId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Receiver does not have a Player profile"));

        if (receiverPlayer.getStatus() != UserStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Cannot invite an inactive Player");
        }

        /*
         * Do not allow inviting yourself.
         */
        if (sender.getUserId().equals(receiver.getUserId())) {

            throw new IllegalArgumentException(
                    "Team creator cannot invite themselves");
        }

        /*
         * If already an ACTIVE member, no invitation is needed.
         */
        TeamPlayer existingMembership =
                teamPlayerRepository
                        .findByTeamTeamIdAndPlayerPlayerId(
                                team.getTeamId(),
                                receiverPlayer.getPlayerId())
                        .orElse(null);

        if (existingMembership != null
                && "ACTIVE".equalsIgnoreCase(
                        existingMembership.getStatus())) {

            throw new IllegalArgumentException(
                    "Player is already a member of this Team");
        }

        /*
         * Prevent duplicate pending invitations.
         */
        
        List<NotificationRequest> receiverRequests =
                notificationRequestRepository
                        .findByReceiverUserIdAndStatus(
                                receiver.getUserId(),
                                "PENDING");

        for (NotificationRequest existing : receiverRequests) {

            if ("PLAYER_INVITATION".equalsIgnoreCase(
                    existing.getRequestType())
                    && existing.getTeam() != null
                    && existing.getTeam()
                            .getTeamId()
                            .equals(team.getTeamId())) {

                throw new IllegalArgumentException(
                        "A pending invitation already exists for this player and team");
            }
        }

        NotificationRequest request =
                new NotificationRequest();

        request.setSender(sender);
        request.setReceiver(receiver);
        request.setTeam(team);
        request.setBooking(null);
        request.setRequestType("PLAYER_INVITATION");
        request.setMessage(requestDTO.getMessage());
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());

        NotificationRequest saved =
                notificationRequestRepository.save(request);

        /*
         * Also create an informational Notification.
         */
        createNotification(
                receiver,
                "Team Invitation",
                requestDTO.getMessage(),
                "PLAYER_INVITATION");

        return mapToResponseDTO(saved);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationRequestResponseDTO> getAllRequests() {

        return notificationRequestRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public NotificationRequestResponseDTO getRequestById(
            String notificationRequestId) {

        NotificationRequest request =
                notificationRequestRepository
                        .findById(notificationRequestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification request not found"));

        return mapToResponseDTO(request);
    }

    // ============================================================
    // GET BY USER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationRequestResponseDTO> getRequestsByUser(
            String userId) {

        if (!userRepository.existsById(userId)) {

            throw new IllegalArgumentException(
                    "User not found");
        }

        return notificationRequestRepository
                .findByReceiverUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // GET PENDING BY USER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationRequestResponseDTO> getPendingRequestsByUser(
            String userId) {

        if (!userRepository.existsById(userId)) {

            throw new IllegalArgumentException(
                    "User not found");
        }

        return notificationRequestRepository
                .findByReceiverUserIdAndStatus(
                        userId,
                        "PENDING")
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // ACCEPT PLAYER INVITATION
    // ============================================================

    @Override
    public NotificationRequestResponseDTO acceptPlayerInvitation(
            String notificationRequestId,
            String userId) {

        NotificationRequest request =
                getRequestForReceiver(
                        notificationRequestId,
                        userId);

        if (!"PLAYER_INVITATION".equalsIgnoreCase(
                request.getRequestType())) {

            throw new IllegalArgumentException(
                    "This request is not a player invitation");
        }

        validatePending(request);

        if (request.getTeam() == null) {

            throw new IllegalArgumentException(
                    "Team is missing from player invitation");
        }

        Player player =
                playerRepository
                        .findByUserUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Player profile not found"));

        Team team = request.getTeam();

        if (!"ACTIVE".equalsIgnoreCase(team.getStatus())) {

            throw new IllegalArgumentException(
                    "Cannot accept invitation to an inactive Team");
        }

        if (player.getStatus() != UserStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Inactive Player cannot accept invitation");
        }

        /*
         * Existing membership:
         * INACTIVE -> reactivate
         * ACTIVE   -> already member
         */
        TeamPlayer membership =
                teamPlayerRepository
                        .findByTeamTeamIdAndPlayerPlayerId(
                                team.getTeamId(),
                                player.getPlayerId())
                        .orElse(null);

        if (membership == null) {

            membership = new TeamPlayer();

            membership.setTeam(team);
            membership.setPlayer(player);
            membership.setJoinedAt(LocalDateTime.now());
            membership.setStatus("ACTIVE");

        } else {

            if ("ACTIVE".equalsIgnoreCase(
                    membership.getStatus())) {

                throw new IllegalArgumentException(
                        "Player is already a member of this Team");
            }

            membership.setStatus("ACTIVE");
        }

        teamPlayerRepository.save(membership);

        request.setStatus("ACCEPTED");

        NotificationRequest saved =
                notificationRequestRepository.save(request);

        /*
         * Inform Team creator.
         */
        createNotification(
                team.getCreatedBy().getUser(),
                "Player Joined Team",
                player.getName()
                        + " accepted your invitation to join "
                        + team.getName(),
                "PLAYER_INVITATION_ACCEPTED");

        return mapToResponseDTO(saved);
    }

    // ============================================================
    // REJECT PLAYER INVITATION
    // ============================================================

    @Override
    public NotificationRequestResponseDTO rejectPlayerInvitation(
            String notificationRequestId,
            String userId) {

        NotificationRequest request =
                getRequestForReceiver(
                        notificationRequestId,
                        userId);

        if (!"PLAYER_INVITATION".equalsIgnoreCase(
                request.getRequestType())) {

            throw new IllegalArgumentException(
                    "This request is not a player invitation");
        }

        validatePending(request);

        request.setStatus("REJECTED");

        NotificationRequest saved =
                notificationRequestRepository.save(request);

        if (request.getTeam() != null) {

            createNotification(
                    request.getTeam().getCreatedBy().getUser(),
                    "Team Invitation Rejected",
                    request.getReceiver().getName()
                            + " rejected your invitation to join "
                            + request.getTeam().getName(),
                    "PLAYER_INVITATION_REJECTED");
        }

        return mapToResponseDTO(saved);
    }

    // ============================================================
    // CREATE COACH REQUESTS
    // ============================================================

    @Override
    public List<NotificationRequestResponseDTO>
            createCoachRequestsForBooking(
                    String bookingId) {

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking not found"));

        if (!Boolean.TRUE.equals(
                booking.getNeedCoach())) {

            throw new IllegalArgumentException(
                    "Coach is not required for this booking");
        }

        if (booking.getCoach() != null) {

            throw new IllegalArgumentException(
                    "A coach is already assigned to this booking");
        }

        Payment payment =
                paymentRepository
                        .findByBookingBookingId(bookingId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found for this booking"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {

            throw new IllegalArgumentException(
                    "Coach requests can only be created after successful payment");
        }

        if (booking.getSlot() == null
                || booking.getSlot().getPlayingArea() == null
                || booking.getSlot()
                        .getPlayingArea()
                        .getTurfSport() == null) {

            throw new IllegalArgumentException(
                    "Booking does not have a valid TurfSport");
        }

        String turfSportId =
                booking.getSlot()
                        .getPlayingArea()
                        .getTurfSport()
                        .getTurfSportId();

        List<Coach> coaches =
                coachRepository
                        .findByTurfSportTurfSportId(
                                turfSportId);

        List<NotificationRequestResponseDTO> responses =
                new java.util.ArrayList<>();

        for (Coach coach : coaches) {

            if (coach.getStatus() != UserStatus.ACTIVE) {
                continue;
            }

            /*
             * Prevent duplicate request to same coach.
             */
            if (notificationRequestRepository
                    .existsByBookingBookingIdAndReceiverUserId(
                            bookingId,
                            coach.getUser().getUserId())) {

                continue;
            }

            NotificationRequest request =
                    new NotificationRequest();

            /*
             * Sender = Player who made the booking.
             */
            request.setSender(
                    booking.getPlayer().getUser());

            /*
             * Receiver = Coach.
             */
            request.setReceiver(
                    coach.getUser());

            request.setTeam(null);
            request.setBooking(booking);
            request.setRequestType("COACH_REQUEST");
            request.setMessage(
                    "A player has requested a coach for a booking at your assigned TurfSport.");
            request.setStatus("PENDING");
            request.setCreatedAt(LocalDateTime.now());

            NotificationRequest saved =
                    notificationRequestRepository.save(request);

            createNotification(
                    coach.getUser(),
                    "Coach Request",
                    request.getMessage(),
                    "COACH_REQUEST");

            responses.add(
                    mapToResponseDTO(saved));
        }
        if (responses.isEmpty()) {

            processCoachUnavailable(booking);
        }
        /*
         * ZERO ELIGIBLE COACHES:
         *
         * We do NOT refund here yet.
         *
         * Payment/booking lifecycle will be connected
         * separately so that we avoid a circular dependency
         * between PaymentService and NotificationRequestService.
         */
        return responses;
    }

    // ============================================================
    // ACCEPT COACH REQUEST
    // ============================================================

    @Override
    public NotificationRequestResponseDTO acceptCoachRequest(
            String notificationRequestId,
            String coachUserId) {

        NotificationRequest request =
                getRequestForReceiver(
                        notificationRequestId,
                        coachUserId);

        if (!"COACH_REQUEST".equalsIgnoreCase(
                request.getRequestType())) {

            throw new IllegalArgumentException(
                    "This request is not a coach request");
        }

        validatePending(request);

        if (request.getBooking() == null) {

            throw new IllegalArgumentException(
                    "Booking is missing from coach request");
        }

        Coach coach =
                coachRepository
                        .findByUserUserId(coachUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Coach profile not found"));

        if (coach.getStatus() != UserStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Inactive Coach cannot accept a request");
        }

        Booking booking =
                bookingRepository
                        .findByBookingIdForUpdate(
                                request.getBooking().getBookingId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking not found"));

        /*
         * FIRST COACH WINS.
         *
         * The Booking lock will be added before we test
         * concurrent acceptance.
         */
        if (booking.getCoach() != null) {

            request.setStatus("REJECTED");

            NotificationRequest saved =
                    notificationRequestRepository.save(request);

            return mapToResponseDTO(saved);
        }

        booking.setCoach(coach);

        /*
         * Keep booking status unchanged here.
         * Coach assignment is separate from booking confirmation.
         */
        bookingRepository.save(booking);

        request.setStatus("ACCEPTED");

        NotificationRequest saved =
                notificationRequestRepository.save(request);

        /*
         * Close all remaining pending coach requests.
         */
        List<NotificationRequest> pendingRequests =
                notificationRequestRepository
                        .findByBookingBookingIdAndStatus(
                                booking.getBookingId(),
                                "PENDING");

        for (NotificationRequest other :
                pendingRequests) {

            if (!other.getNotificationRequestId()
                    .equals(request.getNotificationRequestId())) {

                other.setStatus("REJECTED");

                notificationRequestRepository.save(other);
            }
        }

        /*
         * Notify player.
         */
        createNotification(
                booking.getPlayer().getUser(),
                "Coach Assigned",
                coach.getName()
                        + " has accepted your coach request.",
                "COACH_ASSIGNED");

        return mapToResponseDTO(saved);
    }

    // ============================================================
    // REJECT COACH REQUEST
    // ============================================================

    @Override
    public NotificationRequestResponseDTO rejectCoachRequest(
            String notificationRequestId,
            String coachUserId) {

        NotificationRequest request =
                getRequestForReceiver(
                        notificationRequestId,
                        coachUserId);

        if (!"COACH_REQUEST".equalsIgnoreCase(
                request.getRequestType())) {

            throw new IllegalArgumentException(
                    "This request is not a coach request");
        }

        validatePending(request);

        request.setStatus("REJECTED");

        NotificationRequest saved =
                notificationRequestRepository.save(request);

        Booking booking = request.getBooking();

        /*
         * Check whether another coach is still pending.
         */
        List<NotificationRequest> pendingRequests =
                notificationRequestRepository
                        .findByBookingBookingIdAndStatus(
                                booking.getBookingId(),
                                "PENDING");

        /*
         * If nobody else is pending and nobody accepted,
         * the booking cannot receive a coach.
         *
         * IMPORTANT:
         * Refund/cancellation will be connected in the
         * workflow layer after this basic module compiles.
         */
        if (pendingRequests.isEmpty()
                && booking.getCoach() == null) {

            processCoachUnavailable(booking);
        }

        return mapToResponseDTO(saved);
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private User getUser(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));
    }

    private NotificationRequest getRequestForReceiver(
            String requestId,
            String userId) {

        return notificationRequestRepository
                .findByNotificationRequestIdAndReceiverUserId(
                        requestId,
                        userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Notification request not found for this user"));
    }

    private void validatePending(
            NotificationRequest request) {

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus())) {

            throw new IllegalArgumentException(
                    "Only PENDING requests can be accepted or rejected");
        }
    }

    private void createNotification(
            User user,
            String title,
            String message,
            String type) {

        Notification notification =
                new Notification();

        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
    private void processCoachUnavailable(Booking booking) {

        Payment payment = paymentRepository
                .findByBookingBookingId(booking.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found"));

        // Refund only if payment was successful
        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        // Cancel booking
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCoach(null);

        bookingRepository.save(booking);

        // Player notification
        createNotification(
                booking.getPlayer().getUser(),
                "Coach Unavailable",
                "Sorry, all coaches rejected your request. "
                        + "Your booking has been cancelled and the full amount has been refunded.",
                "COACH_UNAVAILABLE");
    }
    private NotificationRequestResponseDTO mapToResponseDTO(
            NotificationRequest request) {

        NotificationRequestResponseDTO response =
                new NotificationRequestResponseDTO();

        response.setNotificationRequestId(
                request.getNotificationRequestId());

        if (request.getSender() != null) {

            response.setSenderUserId(
                    request.getSender().getUserId());
        }

        if (request.getReceiver() != null) {

            response.setReceiverUserId(
                    request.getReceiver().getUserId());
        }

        if (request.getTeam() != null) {

            response.setTeamId(
                    request.getTeam().getTeamId());
        }

        if (request.getBooking() != null) {

            response.setBookingId(
                    request.getBooking().getBookingId());
        }

        response.setRequestType(
                request.getRequestType());

        response.setMessage(
                request.getMessage());

        response.setStatus(
                request.getStatus());

        response.setCreatedAt(
                request.getCreatedAt());

        return response;
    }
}