package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.BookingRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.BookingResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Slot;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IBookingService;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final PlayerRepository playerRepository;
    private final SlotRepository slotRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            PlayerRepository playerRepository,
            SlotRepository slotRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository) {

        this.bookingRepository = bookingRepository;
        this.playerRepository = playerRepository;
        this.slotRepository = slotRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO requestDTO) {

        Player player = playerRepository
                .findById(requestDTO.getPlayerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found with ID: "
                                        + requestDTO.getPlayerId()));

        validatePlayer(player);

        Slot slot = slotRepository
                .findById(requestDTO.getSlotId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Slot not found with ID: "
                                        + requestDTO.getSlotId()));

        validateSlot(slot);

        validateBookingSlotDetails(requestDTO, slot);

        if (bookingRepository.existsBySlotSlotIdAndStatusIn(
                slot.getSlotId(),
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.CONFIRMED))) {

            throw new IllegalArgumentException(
                    "Slot is already booked");
        }

        Booking booking = new Booking();

        booking.setPlayer(player);
        booking.setSlot(slot);
        booking.setBookingDate(slot.getSlotDate());
        booking.setStartTime(slot.getStartTime());
        booking.setEndTime(slot.getEndTime());

        booking.setNumberOfPlayers(requestDTO.getNumberOfPlayers());
        booking.setPrice(requestDTO.getPrice());
        booking.setDiscount(requestDTO.getDiscount());
        booking.setTax(requestDTO.getTax());
        booking.setTotalAmount(requestDTO.getTotalAmount());

        booking.setStatus(BookingStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();

        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        return mapToResponseDTO(
                bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(String bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with ID: " + bookingId));

        return mapToResponseDTO(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByPlayer(
            String playerId) {

        return bookingRepository
                .findByPlayerPlayerId(playerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsBySlot(
            String slotId) {

        return bookingRepository
                .findBySlotSlotId(slotId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByDate(
            String date) {

        LocalDate bookingDate;

        try {
            bookingDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid date format. Use yyyy-MM-dd");
        }

        return bookingRepository
                .findByBookingDate(bookingDate)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByStatus(
            String status) {

        BookingStatus bookingStatus;

        try {
            bookingStatus =
                    BookingStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid booking status: " + status);
        }

        return bookingRepository
                .findByStatus(bookingStatus)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public BookingResponseDTO updateBooking(
            String bookingId,
            BookingRequestDTO requestDTO) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with ID: "
                                        + bookingId));

        if (!booking.getPlayer()
                .getPlayerId()
                .equals(requestDTO.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Player cannot be changed for an existing booking");
        }

        if (!booking.getSlot()
                .getSlotId()
                .equals(requestDTO.getSlotId())) {

            throw new IllegalArgumentException(
                    "Slot cannot be changed for an existing booking");
        }

        Player player = playerRepository
                .findById(requestDTO.getPlayerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found with ID: "
                                        + requestDTO.getPlayerId()));

        validatePlayer(player);

        Slot slot = slotRepository
                .findById(requestDTO.getSlotId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Slot not found with ID: "
                                        + requestDTO.getSlotId()));

        validateSlot(slot);

        validateBookingSlotDetails(requestDTO, slot);

        if (booking.getStatus() == BookingStatus.CANCELLED) {

            if (bookingRepository.existsBySlotSlotIdAndStatusIn(
                    slot.getSlotId(),
                    List.of(
                            BookingStatus.PENDING,
                            BookingStatus.CONFIRMED))) {

                throw new IllegalArgumentException(
                        "Slot is already booked by another active booking");
            }

            booking.setStatus(BookingStatus.PENDING);
        }

        booking.setNumberOfPlayers(
                requestDTO.getNumberOfPlayers());

        booking.setPrice(requestDTO.getPrice());
        booking.setDiscount(requestDTO.getDiscount());
        booking.setTax(requestDTO.getTax());
        booking.setTotalAmount(requestDTO.getTotalAmount());

        booking.setUpdatedAt(LocalDateTime.now());

        return mapToResponseDTO(
                bookingRepository.save(booking));
    }

    @Override
    public void cancelBooking(String bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with ID: "
                                        + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }

    private void validatePlayer(Player player) {

        if (player.getStatus() != UserStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Player must be ACTIVE");
        }

        if (!hasActivePlayerRole(player.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Player does not have an active PLAYER role");
        }
    }

    private boolean hasActivePlayerRole(String playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found with ID: " + playerId));

        String userId = player.getUser().getUserId();

        return roleRepository.findByRoleName("PLAYER")
                .map(role ->
                        userRoleRepository
                                .findByUserUserId(userId)
                                .stream()
                                .anyMatch(userRole ->
                                        userRole.getRole()
                                                .getRoleId()
                                                .equals(role.getRoleId())
                                                && userRole.getStatus()
                                                == UserStatus.ACTIVE))
                .orElse(false);
    }

    private void validateSlot(Slot slot) {

        if (!"ACTIVE".equalsIgnoreCase(slot.getStatus())) {

            throw new IllegalArgumentException(
                    "Slot must be ACTIVE");
        }

        if (!"ACTIVE".equalsIgnoreCase(
                slot.getPlayingArea().getStatus())) {

            throw new IllegalArgumentException(
                    "Playing area must be ACTIVE");
        }
    }

    private void validateBookingSlotDetails(
            BookingRequestDTO requestDTO,
            Slot slot) {

        if (!requestDTO.getBookingDate()
                .equals(slot.getSlotDate())) {

            throw new IllegalArgumentException(
                    "Booking date must match slot date");
        }

        if (!requestDTO.getStartTime()
                .equals(slot.getStartTime())
                ||
            !requestDTO.getEndTime()
                .equals(slot.getEndTime())) {

            throw new IllegalArgumentException(
                    "Booking time must match slot time");
        }
    }

    private BookingResponseDTO mapToResponseDTO(
            Booking booking) {

        BookingResponseDTO response = new BookingResponseDTO();

        response.setBookingId(booking.getBookingId());

        response.setPlayerId(
                booking.getPlayer().getPlayerId());

        response.setSlotId(
                booking.getSlot().getSlotId());

        response.setBookingDate(
                booking.getBookingDate());

        response.setStartTime(
                booking.getStartTime());

        response.setEndTime(
                booking.getEndTime());

        response.setNumberOfPlayers(
                booking.getNumberOfPlayers());

        response.setPrice(
                booking.getPrice());

        response.setDiscount(
                booking.getDiscount());

        response.setTax(
                booking.getTax());

        response.setTotalAmount(
                booking.getTotalAmount());

        response.setStatus(
                booking.getStatus());

        response.setCreatedAt(
                booking.getCreatedAt());

        response.setUpdatedAt(
                booking.getUpdatedAt());

        return response;
    }
}