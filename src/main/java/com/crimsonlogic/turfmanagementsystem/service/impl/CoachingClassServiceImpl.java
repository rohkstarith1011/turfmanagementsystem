package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachingClassRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachingClassResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Coach;
import com.crimsonlogic.turfmanagementsystem.entity.CoachingClass;
import com.crimsonlogic.turfmanagementsystem.entity.TurfSport;
import com.crimsonlogic.turfmanagementsystem.repository.CoachRepository;
import com.crimsonlogic.turfmanagementsystem.repository.CoachingClassRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfSportRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICoachingClassService;

@Service
@Transactional
public class CoachingClassServiceImpl
        implements ICoachingClassService {

    private final CoachingClassRepository coachingClassRepository;
    private final CoachRepository coachRepository;
    private final TurfSportRepository turfSportRepository;

    public CoachingClassServiceImpl(
            CoachingClassRepository coachingClassRepository,
            CoachRepository coachRepository,
            TurfSportRepository turfSportRepository) {

        this.coachingClassRepository = coachingClassRepository;
        this.coachRepository = coachRepository;
        this.turfSportRepository = turfSportRepository;
    }

    @Override
    public CoachingClassResponseDTO createCoachingClass(
            CoachingClassRequestDTO requestDTO) {

        Coach coach = coachRepository
                .findById(requestDTO.getCoachId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Coach not found"));

        if (!"ACTIVE".equalsIgnoreCase(coach.getStatus().name())) {
            throw new IllegalArgumentException(
                    "Cannot create Coaching Class using an inactive Coach");
        }

        TurfSport turfSport = turfSportRepository
                .findById(requestDTO.getTurfSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Turf Sport not found"));

        if (!"ACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot create Coaching Class for an inactive Turf Sport");
        }

        if (!coach.getTurfSport().getTurfSportId()
                .equals(turfSport.getTurfSportId())) {

            throw new IllegalArgumentException(
                    "Coach is not assigned to the selected Turf Sport");
        }

        validateDates(requestDTO.getStartDate(),
                requestDTO.getEndDate());

        CoachingClass coachingClass = new CoachingClass();

        coachingClass.setCoach(coach);
        coachingClass.setTurfSport(turfSport);
        coachingClass.setName(requestDTO.getName());
        coachingClass.setDescription(requestDTO.getDescription());
        coachingClass.setClassType(requestDTO.getClassType());
        coachingClass.setRegistrationLimit(
                requestDTO.getRegistrationLimit());
        coachingClass.setFee(requestDTO.getFee());
        coachingClass.setStartDate(requestDTO.getStartDate());
        coachingClass.setEndDate(requestDTO.getEndDate());
        coachingClass.setStatus("ACTIVE");

        return mapToResponseDTO(
                coachingClassRepository.save(coachingClass));
    }

    @Override
    @Transactional(readOnly = true)
    public CoachingClassResponseDTO getCoachingClassById(
            String coachingClassId) {

        CoachingClass coachingClass =
                coachingClassRepository.findById(coachingClassId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Coaching Class not found"));

        return mapToResponseDTO(coachingClass);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassResponseDTO> getAllCoachingClasses() {

        return coachingClassRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassResponseDTO>
            getCoachingClassesByCoach(String coachId) {

        return coachingClassRepository
                .findByCoachCoachId(coachId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassResponseDTO>
            getCoachingClassesByTurfSport(String turfSportId) {

        return coachingClassRepository
                .findByTurfSportTurfSportId(turfSportId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public CoachingClassResponseDTO updateCoachingClass(
            String coachingClassId,
            CoachingClassRequestDTO requestDTO) {

        CoachingClass coachingClass =
                coachingClassRepository.findById(coachingClassId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Coaching Class not found"));

        Coach coach = coachRepository
                .findById(requestDTO.getCoachId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Coach not found"));

        if (!coachingClass.getCoach().getCoachId()
                .equals(requestDTO.getCoachId())) {

            throw new IllegalArgumentException(
                    "Coaching Class cannot be reassigned to another Coach");
        }

        if (!"ACTIVE".equalsIgnoreCase(coach.getStatus().name())) {
            throw new IllegalArgumentException(
                    "Cannot update Coaching Class using an inactive Coach");
        }

        TurfSport turfSport = turfSportRepository
                .findById(requestDTO.getTurfSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Turf Sport not found"));

        if (!coachingClass.getTurfSport().getTurfSportId()
                .equals(requestDTO.getTurfSportId())) {

            throw new IllegalArgumentException(
                    "Coaching Class cannot be reassigned to another Turf Sport");
        }

        if (!"ACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Coaching Class with an inactive Turf Sport");
        }

        if (!coach.getTurfSport().getTurfSportId()
                .equals(turfSport.getTurfSportId())) {

            throw new IllegalArgumentException(
                    "Coach is not assigned to the selected Turf Sport");
        }

        validateDates(requestDTO.getStartDate(),
                requestDTO.getEndDate());

        coachingClass.setCoach(coach);
        coachingClass.setTurfSport(turfSport);
        coachingClass.setName(requestDTO.getName());
        coachingClass.setDescription(requestDTO.getDescription());
        coachingClass.setClassType(requestDTO.getClassType());
        coachingClass.setRegistrationLimit(
                requestDTO.getRegistrationLimit());
        coachingClass.setFee(requestDTO.getFee());
        coachingClass.setStartDate(requestDTO.getStartDate());
        coachingClass.setEndDate(requestDTO.getEndDate());

        if ("INACTIVE".equalsIgnoreCase(
                coachingClass.getStatus())) {

            coachingClass.setStatus("ACTIVE");
        }

        return mapToResponseDTO(
                coachingClassRepository.save(coachingClass));
    }

    @Override
    public void deactivateCoachingClass(
            String coachingClassId) {

        CoachingClass coachingClass =
                coachingClassRepository.findById(coachingClassId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Coaching Class not found"));

        if ("INACTIVE".equalsIgnoreCase(
                coachingClass.getStatus())) {

            throw new IllegalArgumentException(
                    "Coaching Class is already inactive");
        }

        coachingClass.setStatus("INACTIVE");

        coachingClassRepository.save(coachingClass);
    }

    private void validateDates(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException(
                    "Start date must be before end date");
        }

        if (startDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Coaching Class start date cannot be in the past");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassResponseDTO> searchCoachingClassesByName(
            String name) {

        return coachingClassRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassResponseDTO> searchCoachingClassesByCoachName(
            String name) {

        return coachingClassRepository
                .findByCoachNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassResponseDTO> searchCoachingClassesByTurfName(
            String name) {

        return coachingClassRepository
                .findByTurfSportFacilityNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private CoachingClassResponseDTO mapToResponseDTO(
            CoachingClass coachingClass) {

        CoachingClassResponseDTO responseDTO =
                new CoachingClassResponseDTO();

        responseDTO.setCoachingClassId(
                coachingClass.getCoachingClassId());

        responseDTO.setCoachId(
                coachingClass.getCoach().getCoachId());

        responseDTO.setTurfSportId(
                coachingClass.getTurfSport().getTurfSportId());

        responseDTO.setName(
                coachingClass.getName());

        responseDTO.setDescription(
                coachingClass.getDescription());

        responseDTO.setClassType(
                coachingClass.getClassType());

        responseDTO.setRegistrationLimit(
                coachingClass.getRegistrationLimit());

        responseDTO.setFee(
                coachingClass.getFee());

        responseDTO.setStartDate(
                coachingClass.getStartDate());

        responseDTO.setEndDate(
                coachingClass.getEndDate());

        responseDTO.setStatus(
                coachingClass.getStatus());

        return responseDTO;
    }
}