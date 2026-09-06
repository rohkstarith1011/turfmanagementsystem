package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.FacilityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityResponseDTO;

import java.util.List;

public interface IFacilityService {

    FacilityResponseDTO createFacility(FacilityRequestDTO requestDTO);

    List<FacilityResponseDTO> getAllFacilities();

    FacilityResponseDTO getFacilityById(String facilityId);

    List<FacilityResponseDTO> getFacilitiesByName(String name);

    List<FacilityResponseDTO> getFacilitiesByCity(String city);

    List<FacilityResponseDTO> getFacilitiesByState(String state);

    List<FacilityResponseDTO> getFacilitiesByLocality(String locality);

    List<FacilityResponseDTO> getFacilitiesByOwnerId(String ownerId);

    List<FacilityResponseDTO> getFacilitiesByManagerId(String managerId);

    FacilityResponseDTO updateFacility(
            String facilityId,
            FacilityRequestDTO requestDTO);

    void deactivateFacility(String facilityId);
}