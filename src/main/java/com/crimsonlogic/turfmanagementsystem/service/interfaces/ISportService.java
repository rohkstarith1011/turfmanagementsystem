package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SportResponseDTO;

import java.util.List;

public interface ISportService {

    SportResponseDTO createSport(SportRequestDTO requestDTO);

    List<SportResponseDTO> getAllSports();

    SportResponseDTO getSportById(String sportId);

    SportResponseDTO getSportByName(String name);

    SportResponseDTO updateSport(String sportId, SportRequestDTO requestDTO);

    void deactivateSport(String sportId);
}