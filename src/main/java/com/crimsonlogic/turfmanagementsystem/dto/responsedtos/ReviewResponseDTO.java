package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import java.time.LocalDateTime;

public class ReviewResponseDTO {

    private String reviewId;
    private String playerId;
    private String facilityId;
    private String bookingId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(String reviewId,
                              String playerId,
                              String facilityId,
                              String bookingId,
                              Integer rating,
                              String comment,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
        this.reviewId = reviewId;
        this.playerId = playerId;
        this.facilityId = facilityId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}