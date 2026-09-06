package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfManagerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfManagerResponseDTO;

import java.util.List;

public interface ITurfManagerService {

    TurfManagerResponseDTO createTurfManager(
            TurfManagerRequestDTO requestDTO);

    List<TurfManagerResponseDTO> getAllTurfManagers();

    TurfManagerResponseDTO getTurfManagerById(
            String turfManagerId);

    TurfManagerResponseDTO getTurfManagerByUserId(
            String userId);

    List<TurfManagerResponseDTO> getTurfManagersByName(
            String name);

    TurfManagerResponseDTO updateTurfManager(
            String turfManagerId,
            TurfManagerRequestDTO requestDTO);

    void deactivateTurfManager(
            String turfManagerId);
}