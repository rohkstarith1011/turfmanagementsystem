package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachingClassRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachingClassResponseDTO;

public interface ICoachingClassService {

    CoachingClassResponseDTO createCoachingClass(
            CoachingClassRequestDTO requestDTO);

    CoachingClassResponseDTO getCoachingClassById(
            String coachingClassId);

    List<CoachingClassResponseDTO> getAllCoachingClasses();

    List<CoachingClassResponseDTO> getCoachingClassesByCoach(
            String coachId);

    List<CoachingClassResponseDTO> getCoachingClassesByTurfSport(
            String turfSportId);

    CoachingClassResponseDTO updateCoachingClass(
            String coachingClassId,
            CoachingClassRequestDTO requestDTO);

    void deactivateCoachingClass(
            String coachingClassId);
    
    List<CoachingClassResponseDTO> searchCoachingClassesByName(String name);

    List<CoachingClassResponseDTO> searchCoachingClassesByCoachName(String name);

    List<CoachingClassResponseDTO> searchCoachingClassesByTurfName(String name);
}