package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.BookingPlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.BookingPlayerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.BookingPlayer;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.BookingPlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IBookingPlayerService;

@Service
public class BookingPlayerServiceImpl
        implements IBookingPlayerService {

    private final BookingPlayerRepository bookingPlayerRepository;
    private final BookingRepository bookingRepository;
    private final PlayerRepository playerRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public BookingPlayerServiceImpl(
            BookingPlayerRepository bookingPlayerRepository,
            BookingRepository bookingRepository,
            PlayerRepository playerRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository) {

        this.bookingPlayerRepository = bookingPlayerRepository;
        this.bookingRepository = bookingRepository;
        this.playerRepository = playerRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    // =========================
    // CREATE BOOKING PLAYER
    // =========================

    @Override
    @Transactional
    public BookingPlayerResponseDTO createBookingPlayer(
            BookingPlayerRequestDTO requestDTO) {

        Booking booking = bookingRepository
                .findById(requestDTO.getBookingId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cannot add player to a cancelled booking");
        }

        Player player = playerRepository
                .findById(requestDTO.getPlayerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found"));

        if (player.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Player must be ACTIVE");
        }

        if (!hasActivePlayerRole(
                player.getUser().getUserId())) {

            throw new IllegalArgumentException(
                    "Player does not have an ACTIVE PLAYER role");
        }

        boolean alreadyAdded =
                bookingPlayerRepository
                        .existsByBookingBookingIdAndPlayerPlayerId(
                                booking.getBookingId(),
                                player.getPlayerId());

        if (alreadyAdded) {
            throw new IllegalArgumentException(
                    "Player is already added to this booking");
        }

        long activePlayers =
                bookingPlayerRepository
                        .findByBookingBookingId(
                                booking.getBookingId())
                        .stream()
                        .filter(bp ->
                                "ACTIVE".equalsIgnoreCase(
                                        bp.getStatus()))
                        .count();

        if (activePlayers >= booking.getNumberOfPlayers()) {
            throw new IllegalArgumentException(
                    "Booking player limit is full");
        }

        BookingPlayer bookingPlayer =
                new BookingPlayer();

        bookingPlayer.setBooking(booking);
        bookingPlayer.setPlayer(player);

        // Always derive the name from Player
        bookingPlayer.setPlayerName(player.getName());

        bookingPlayer.setStatus("ACTIVE");

        BookingPlayer saved =
                bookingPlayerRepository.save(bookingPlayer);

        return mapToResponseDTO(saved);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public BookingPlayerResponseDTO getBookingPlayerById(
            String bookingPlayerId) {

        BookingPlayer bookingPlayer =
                bookingPlayerRepository
                        .findById(bookingPlayerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking player not found"));

        return mapToResponseDTO(bookingPlayer);
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<BookingPlayerResponseDTO> getAllBookingPlayers() {

        return bookingPlayerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // GET BY BOOKING
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<BookingPlayerResponseDTO>
    getBookingPlayersByBooking(String bookingId) {

        return bookingPlayerRepository
                .findByBookingBookingId(bookingId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // GET BY PLAYER
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<BookingPlayerResponseDTO>
    getBookingPlayersByPlayer(String playerId) {

        return bookingPlayerRepository
                .findByPlayerPlayerId(playerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    @Transactional
    public BookingPlayerResponseDTO updateBookingPlayer(
            String bookingPlayerId,
            BookingPlayerRequestDTO requestDTO) {

        BookingPlayer bookingPlayer =
                bookingPlayerRepository
                        .findById(bookingPlayerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking player not found"));

        // Booking cannot be changed
        if (!bookingPlayer.getBooking()
                .getBookingId()
                .equals(requestDTO.getBookingId())) {

            throw new IllegalArgumentException(
                    "Booking cannot be changed");
        }

        // Player cannot be changed
        if (!bookingPlayer.getPlayer()
                .getPlayerId()
                .equals(requestDTO.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Player cannot be changed");
        }

        Booking booking =
                bookingPlayer.getBooking();

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cannot update player for a cancelled booking");
        }

        Player player =
                bookingPlayer.getPlayer();

        if (player.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Player must be ACTIVE");
        }

        if (!hasActivePlayerRole(
                player.getUser().getUserId())) {

            throw new IllegalArgumentException(
                    "Player does not have an ACTIVE PLAYER role");
        }

        // Reactivate inactive BookingPlayer
        if ("INACTIVE".equalsIgnoreCase(
                bookingPlayer.getStatus())) {

            long activePlayers =
                    bookingPlayerRepository
                            .findByBookingBookingId(
                                    booking.getBookingId())
                            .stream()
                            .filter(bp ->
                                    "ACTIVE".equalsIgnoreCase(
                                            bp.getStatus()))
                            .count();

            if (activePlayers >= booking.getNumberOfPlayers()) {
                throw new IllegalArgumentException(
                        "Booking player limit is full");
            }

            bookingPlayer.setStatus("ACTIVE");
        }

        // Keep name synchronized with Player
        bookingPlayer.setPlayerName(player.getName());

        BookingPlayer updated =
                bookingPlayerRepository.save(bookingPlayer);

        return mapToResponseDTO(updated);
    }

    // =========================
    // DEACTIVATE
    // =========================

    @Override
    @Transactional
    public void deactivateBookingPlayer(
            String bookingPlayerId) {

        BookingPlayer bookingPlayer =
                bookingPlayerRepository
                        .findById(bookingPlayerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking player not found"));

        if ("INACTIVE".equalsIgnoreCase(
                bookingPlayer.getStatus())) {

            throw new IllegalArgumentException(
                    "Booking player is already INACTIVE");
        }

        bookingPlayer.setStatus("INACTIVE");

        bookingPlayerRepository.save(bookingPlayer);
    }

    // =========================
    // PLAYER ROLE CHECK
    // =========================

    private boolean hasActivePlayerRole(String userId) {

        Role playerRole =
                roleRepository
                        .findByRoleName("PLAYER")
                        .orElse(null);

        if (playerRole == null) {
            return false;
        }

        return userRoleRepository
                .findByUserUserId(userId)
                .stream()
                .anyMatch(userRole ->
                        userRole.getRole()
                                .getRoleId()
                                .equals(playerRole.getRoleId())
                        &&
                        userRole.getStatus() == UserStatus.ACTIVE);
    }

    // =========================
    // ENTITY → RESPONSE DTO
    // =========================

    private BookingPlayerResponseDTO mapToResponseDTO(
            BookingPlayer bookingPlayer) {

        BookingPlayerResponseDTO response =
                new BookingPlayerResponseDTO();

        response.setBookingPlayerId(
                bookingPlayer.getBookingPlayerId());

        response.setBookingId(
                bookingPlayer.getBooking()
                        .getBookingId());

        response.setPlayerId(
                bookingPlayer.getPlayer()
                        .getPlayerId());

        response.setPlayerName(
                bookingPlayer.getPlayerName());

        response.setStatus(
                bookingPlayer.getStatus());

        return response;
    }
}