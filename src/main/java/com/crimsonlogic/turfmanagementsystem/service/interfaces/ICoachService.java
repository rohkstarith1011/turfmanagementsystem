package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachResponseDTO;

public interface ICoachService {

    CoachResponseDTO createCoach(CoachRequestDTO requestDTO);

    CoachResponseDTO getCoachById(String coachId);

    List<CoachResponseDTO> getAllCoaches();

    List<CoachResponseDTO> getCoachesByTurfSport(String turfSportId);

    CoachResponseDTO updateCoach(String coachId, CoachRequestDTO requestDTO);

    void deactivateCoach(String coachId);
}