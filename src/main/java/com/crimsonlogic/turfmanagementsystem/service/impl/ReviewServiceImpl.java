package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.ReviewRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.ReviewResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Review;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.ReviewRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IReviewService;

@Service
@Transactional
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final PlayerRepository playerRepository;
    private final FacilityRepository facilityRepository;
    private final BookingRepository bookingRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            PlayerRepository playerRepository,
            FacilityRepository facilityRepository,
            BookingRepository bookingRepository) {

        this.reviewRepository = reviewRepository;
        this.playerRepository = playerRepository;
        this.facilityRepository = facilityRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO requestDTO) {

        Player player = playerRepository
                .findById(requestDTO.getPlayerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found with ID: "
                                        + requestDTO.getPlayerId()));

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found with ID: "
                                        + requestDTO.getFacilityId()));

        Booking booking = bookingRepository
                .findById(requestDTO.getBookingId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with ID: "
                                        + requestDTO.getBookingId()));

        if (reviewRepository.existsByBookingBookingId(
                requestDTO.getBookingId())) {

            throw new IllegalArgumentException(
                    "A review already exists for this booking");
        }

        if (booking.getPlayer() == null
                || !booking.getPlayer().getPlayerId()
                        .equals(player.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Booking does not belong to the specified player");
        }

        if (booking.getSlot() == null
                || booking.getSlot().getPlayingArea() == null
                || booking.getSlot().getPlayingArea().getFacility() == null
                || !booking.getSlot().getPlayingArea().getFacility()
                        .getFacilityId()
                        .equals(facility.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Booking does not belong to the specified facility");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Review can only be submitted for a completed booking");
        }

        Review review = new Review();

        review.setPlayer(player);
        review.setFacility(facility);
        review.setBooking(booking);
        review.setRating(requestDTO.getRating());
        review.setComment(requestDTO.getComment());

        LocalDateTime now = LocalDateTime.now();

        review.setCreatedAt(now);
        review.setUpdatedAt(now);

        Review savedReview = reviewRepository.save(review);

        updateFacilityRating(facility);

        return mapToResponseDTO(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(String reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found with ID: " + reviewId));

        return mapToResponseDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByFacility(
            String facilityId) {

        if (!facilityRepository.existsById(facilityId)) {

            throw new IllegalArgumentException(
                    "Facility not found with ID: " + facilityId);
        }

        return reviewRepository
                .findByFacilityFacilityId(facilityId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByPlayer(
            String playerId) {

        if (!playerRepository.existsById(playerId)) {

            throw new IllegalArgumentException(
                    "Player not found with ID: " + playerId);
        }

        return reviewRepository
                .findByPlayerPlayerId(playerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO updateReview(
            String reviewId,
            ReviewRequestDTO requestDTO) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found with ID: " + reviewId));

        if (!review.getBooking().getBookingId()
                .equals(requestDTO.getBookingId())) {

            throw new IllegalArgumentException(
                    "Booking cannot be changed for a review");
        }

        if (!review.getPlayer().getPlayerId()
                .equals(requestDTO.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Player cannot be changed for a review");
        }

        if (!review.getFacility().getFacilityId()
                .equals(requestDTO.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Facility cannot be changed for a review");
        }

        review.setRating(requestDTO.getRating());
        review.setComment(requestDTO.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);

        updateFacilityRating(review.getFacility());

        return mapToResponseDTO(updatedReview);
    }

    @Override
    public void deleteReview(String reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found with ID: " + reviewId));

        Facility facility = review.getFacility();

        reviewRepository.delete(review);

        updateFacilityRating(facility);
    }

    private ReviewResponseDTO mapToResponseDTO(Review review) {

        return new ReviewResponseDTO(
                review.getReviewId(),
                review.getPlayer().getPlayerId(),
                review.getFacility().getFacilityId(),
                review.getBooking().getBookingId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }
    private void updateFacilityRating(Facility facility) {

        Double averageRating =
                reviewRepository.findAverageRatingByFacilityId(
                        facility.getFacilityId());

        if (averageRating == null) {
            facility.setRating(0.0);
        } else {
            facility.setRating(
                    Math.round(averageRating * 100.0) / 100.0);
        }

        facilityRepository.save(facility);
    }
}