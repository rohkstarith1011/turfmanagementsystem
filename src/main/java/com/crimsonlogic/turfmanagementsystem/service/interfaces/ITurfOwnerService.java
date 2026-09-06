package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfOwnerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerResponseDTO;

import java.util.List;

public interface ITurfOwnerService {

    TurfOwnerResponseDTO createTurfOwner(TurfOwnerRequestDTO requestDTO);

    List<TurfOwnerResponseDTO> getAllTurfOwners();

    TurfOwnerResponseDTO getTurfOwnerById(String turfOwnerId);

    TurfOwnerResponseDTO getTurfOwnerByUserId(String userId);

    TurfOwnerResponseDTO updateTurfOwner(String turfOwnerId, TurfOwnerRequestDTO requestDTO);

    void deactivateTurfOwner(String turfOwnerId);
    
    List<TurfOwnerResponseDTO> getTurfOwnersByName(String name);
}