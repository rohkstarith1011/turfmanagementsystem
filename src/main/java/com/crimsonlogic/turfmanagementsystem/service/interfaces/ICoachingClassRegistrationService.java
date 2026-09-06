package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachingClassRegistrationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachingClassRegistrationResponseDTO;

import java.util.List;

public interface ICoachingClassRegistrationService {

    CoachingClassRegistrationResponseDTO createRegistration(
            CoachingClassRegistrationRequestDTO requestDTO);

    CoachingClassRegistrationResponseDTO getRegistrationById(
            String registrationId);

    List<CoachingClassRegistrationResponseDTO> getAllRegistrations();

    List<CoachingClassRegistrationResponseDTO> getRegistrationsByCoachingClass(
            String coachingClassId);

    List<CoachingClassRegistrationResponseDTO> getRegistrationsByPlayer(
            String playerId);

    List<CoachingClassRegistrationResponseDTO> searchRegistrationsByCoachingClassName(
            String name);

    List<CoachingClassRegistrationResponseDTO> searchRegistrationsByPlayerName(
            String name);

    CoachingClassRegistrationResponseDTO updateRegistration(
            String registrationId,
            CoachingClassRegistrationRequestDTO requestDTO);

    void deactivateRegistration(String registrationId);
}