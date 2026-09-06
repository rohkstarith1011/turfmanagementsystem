package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfSportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfSportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.Sport;
import com.crimsonlogic.turfmanagementsystem.entity.TurfSport;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SportRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfSportRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfSportService;

@Service
public class TurfSportServiceImpl implements ITurfSportService {

    private final TurfSportRepository turfSportRepository;
    private final FacilityRepository facilityRepository;
    private final SportRepository sportRepository;

    public TurfSportServiceImpl(
            TurfSportRepository turfSportRepository,
            FacilityRepository facilityRepository,
            SportRepository sportRepository) {

        this.turfSportRepository = turfSportRepository;
        this.facilityRepository = facilityRepository;
        this.sportRepository = sportRepository;
    }

    @Override
    public TurfSportResponseDTO createTurfSport(
            TurfSportRequestDTO requestDTO) {

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign sport to an inactive Facility");
        }

        Sport sport = sportRepository
                .findById(requestDTO.getSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sport not found"));

        if (!"ACTIVE".equalsIgnoreCase(sport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive Sport to a Facility");
        }

        if (turfSportRepository
                .existsByFacilityFacilityIdAndSportSportId(
                        requestDTO.getFacilityId(),
                        requestDTO.getSportId())) {

            throw new IllegalArgumentException(
                    "This Sport is already assigned to this Facility");
        }

        TurfSport turfSport = new TurfSport();

        turfSport.setFacility(facility);
        turfSport.setSport(sport);
        turfSport.setStatus("ACTIVE");

        TurfSport savedTurfSport =
                turfSportRepository.save(turfSport);

        return mapToResponseDTO(savedTurfSport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfSportResponseDTO> getAllTurfSports() {

        return turfSportRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TurfSportResponseDTO getTurfSportById(
            String turfSportId) {

        TurfSport turfSport =
                turfSportRepository.findById(turfSportId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Sport not found"));

        return mapToResponseDTO(turfSport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfSportResponseDTO> getTurfSportsByFacilityId(
            String facilityId) {

        return turfSportRepository
                .findByFacilityFacilityId(facilityId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfSportResponseDTO> getTurfSportsBySportId(
            String sportId) {

        return turfSportRepository
                .findBySportSportId(sportId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public TurfSportResponseDTO updateTurfSport(
            String turfSportId,
            TurfSportRequestDTO requestDTO) {

        TurfSport turfSport =
                turfSportRepository.findById(turfSportId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Sport not found"));

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        Sport sport = sportRepository
                .findById(requestDTO.getSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sport not found"));

        if (!turfSport.getFacility().getFacilityId()
                .equals(requestDTO.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Turf Sport cannot be reassigned to another Facility");
        }

        if (!turfSport.getSport().getSportId()
                .equals(requestDTO.getSportId())) {

            throw new IllegalArgumentException(
                    "Turf Sport cannot be reassigned to another Sport");
        }

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Turf Sport for an inactive Facility");
        }

        if (!"ACTIVE".equalsIgnoreCase(sport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Turf Sport with an inactive Sport");
        }

        turfSport.setFacility(facility);
        turfSport.setSport(sport);
        if ("INACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            turfSport.setStatus("ACTIVE");
        }
        TurfSport updatedTurfSport =
                turfSportRepository.save(turfSport);

        return mapToResponseDTO(updatedTurfSport);
    }

    @Override
    public void deactivateTurfSport(
            String turfSportId) {

        TurfSport turfSport =
                turfSportRepository.findById(turfSportId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Sport not found"));

        turfSport.setStatus("INACTIVE");

        turfSportRepository.save(turfSport);
    }

    private TurfSportResponseDTO mapToResponseDTO(
            TurfSport turfSport) {

        TurfSportResponseDTO responseDTO =
                new TurfSportResponseDTO();

        responseDTO.setTurfSportId(
                turfSport.getTurfSportId());

        responseDTO.setFacilityId(
                turfSport.getFacility().getFacilityId());

        responseDTO.setSportId(
                turfSport.getSport().getSportId());

        responseDTO.setStatus(
                turfSport.getStatus());

        return responseDTO;
    }
}