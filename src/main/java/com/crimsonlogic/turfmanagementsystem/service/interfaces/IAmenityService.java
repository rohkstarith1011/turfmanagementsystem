package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AmenityResponseDTO;

import java.util.List;

public interface IAmenityService {

    AmenityResponseDTO createAmenity(AmenityRequestDTO requestDTO);

    List<AmenityResponseDTO> getAllAmenities();

    AmenityResponseDTO getAmenityById(String amenityId);

    AmenityResponseDTO getAmenityByName(String name);

    AmenityResponseDTO updateAmenity(
            String amenityId,
            AmenityRequestDTO requestDTO
    );

    void deactivateAmenity(String amenityId);
}