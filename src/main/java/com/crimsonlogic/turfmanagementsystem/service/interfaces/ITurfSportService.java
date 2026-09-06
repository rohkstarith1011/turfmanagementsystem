package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfSportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfSportResponseDTO;

public interface ITurfSportService {

    TurfSportResponseDTO createTurfSport(
            TurfSportRequestDTO requestDTO);

    List<TurfSportResponseDTO> getAllTurfSports();

    TurfSportResponseDTO getTurfSportById(
            String turfSportId);

    List<TurfSportResponseDTO> getTurfSportsByFacilityId(
            String facilityId);

    List<TurfSportResponseDTO> getTurfSportsBySportId(
            String sportId);

    TurfSportResponseDTO updateTurfSport(
            String turfSportId,
            TurfSportRequestDTO requestDTO);

    void deactivateTurfSport(
            String turfSportId);
}