package com.crimsonlogic.turfmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.ReviewRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.ReviewResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Valid @RequestBody ReviewRequestDTO requestDTO) {

        return new ResponseEntity<>(
                reviewService.createReview(requestDTO),
                HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(
            @PathVariable String reviewId) {

        return ResponseEntity.ok(
                reviewService.getReviewById(reviewId));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByFacility(
            @PathVariable String facilityId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByFacility(facilityId));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByPlayer(
            @PathVariable String playerId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByPlayer(playerId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {

        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, requestDTO));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String reviewId) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.noContent().build();
    }
}