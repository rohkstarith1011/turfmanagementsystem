package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfAmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfAmenityResponseDTO;

public interface ITurfAmenityService {

    TurfAmenityResponseDTO createTurfAmenity(
            TurfAmenityRequestDTO requestDTO);

    List<TurfAmenityResponseDTO> getAllTurfAmenities();

    TurfAmenityResponseDTO getTurfAmenityById(
            String turfAmenityId);

    List<TurfAmenityResponseDTO> getTurfAmenitiesByFacilityId(
            String facilityId);

    List<TurfAmenityResponseDTO> getTurfAmenitiesByAmenityId(
            String amenityId);

    TurfAmenityResponseDTO updateTurfAmenity(
            String turfAmenityId,
            TurfAmenityRequestDTO requestDTO);

    void deactivateTurfAmenity(
            String turfAmenityId);
}