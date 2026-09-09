package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.ReviewRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.ReviewResponseDTO;

public interface IReviewService {

    ReviewResponseDTO createReview(ReviewRequestDTO requestDTO);

    ReviewResponseDTO getReviewById(String reviewId);

    List<ReviewResponseDTO> getReviewsByFacility(String facilityId);

    List<ReviewResponseDTO> getReviewsByPlayer(String playerId);

    ReviewResponseDTO updateReview(String reviewId, ReviewRequestDTO requestDTO);

    void deleteReview(String reviewId);
}