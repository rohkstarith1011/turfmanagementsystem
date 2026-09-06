package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PlayingAreaRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PlayingAreaResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.PlayingArea;
import com.crimsonlogic.turfmanagementsystem.entity.TurfSport;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayingAreaRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfSportRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPlayingAreaService;

@Service
public class PlayingAreaServiceImpl
        implements IPlayingAreaService {

    private final PlayingAreaRepository playingAreaRepository;
    private final FacilityRepository facilityRepository;
    private final TurfSportRepository turfSportRepository;

    public PlayingAreaServiceImpl(
            PlayingAreaRepository playingAreaRepository,
            FacilityRepository facilityRepository,
            TurfSportRepository turfSportRepository) {

        this.playingAreaRepository = playingAreaRepository;
        this.facilityRepository = facilityRepository;
        this.turfSportRepository = turfSportRepository;
    }

    @Override
    public PlayingAreaResponseDTO createPlayingArea(
            PlayingAreaRequestDTO requestDTO) {

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot create Playing Area for an inactive Facility");
        }

        TurfSport turfSport = turfSportRepository
                .findById(requestDTO.getTurfSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Turf Sport not found"));

        if (!"ACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot create Playing Area for an inactive Turf Sport");
        }

        if (!turfSport.getFacility().getFacilityId()
                .equals(facility.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Turf Sport does not belong to the selected Facility");
        }

        boolean duplicate =
                playingAreaRepository
                        .findByTurfSportTurfSportId(
                                requestDTO.getTurfSportId())
                        .stream()
                        .anyMatch(existingArea ->
                                existingArea.getName()
                                        .equalsIgnoreCase(
                                                requestDTO.getName()));

        if (duplicate) {
            throw new IllegalArgumentException(
                    "Playing Area with this name already exists for this Turf Sport");
        }

        PlayingArea playingArea = new PlayingArea();

        playingArea.setFacility(facility);
        playingArea.setTurfSport(turfSport);
        playingArea.setName(requestDTO.getName());
        playingArea.setDescription(requestDTO.getDescription());
        playingArea.setStatus("ACTIVE");

        PlayingArea savedPlayingArea =
                playingAreaRepository.save(playingArea);

        return mapToResponseDTO(savedPlayingArea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayingAreaResponseDTO> getAllPlayingAreas() {

        return playingAreaRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlayingAreaResponseDTO getPlayingAreaById(
            String playingAreaId) {

        PlayingArea playingArea =
                playingAreaRepository.findById(playingAreaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Playing Area not found"));

        return mapToResponseDTO(playingArea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayingAreaResponseDTO>
            getPlayingAreasByFacilityId(String facilityId) {

        return playingAreaRepository
                .findByFacilityFacilityId(facilityId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayingAreaResponseDTO>
            getPlayingAreasByTurfSportId(String turfSportId) {

        return playingAreaRepository
                .findByTurfSportTurfSportId(turfSportId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public PlayingAreaResponseDTO updatePlayingArea(
            String playingAreaId,
            PlayingAreaRequestDTO requestDTO) {

        PlayingArea playingArea =
                playingAreaRepository.findById(playingAreaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Playing Area not found"));

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        TurfSport turfSport = turfSportRepository
                .findById(requestDTO.getTurfSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Turf Sport not found"));

        if (!playingArea.getFacility().getFacilityId()
                .equals(requestDTO.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Playing Area cannot be reassigned to another Facility");
        }

        if (!playingArea.getTurfSport().getTurfSportId()
                .equals(requestDTO.getTurfSportId())) {

            throw new IllegalArgumentException(
                    "Playing Area cannot be reassigned to another Turf Sport");
        }

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Playing Area for an inactive Facility");
        }

        if (!"ACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Playing Area for an inactive Turf Sport");
        }

        if (!turfSport.getFacility().getFacilityId()
                .equals(facility.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Turf Sport does not belong to the selected Facility");
        }

        boolean duplicate =
                playingAreaRepository
                        .findByTurfSportTurfSportId(
                                requestDTO.getTurfSportId())
                        .stream()
                        .anyMatch(existingArea ->
                                !existingArea.getPlayingAreaId()
                                        .equals(playingAreaId)
                                && existingArea.getName()
                                        .equalsIgnoreCase(
                                                requestDTO.getName()));

        if (duplicate) {
            throw new IllegalArgumentException(
                    "Playing Area with this name already exists for this Turf Sport");
        }

        playingArea.setFacility(facility);
        playingArea.setTurfSport(turfSport);
        playingArea.setName(requestDTO.getName());
        playingArea.setDescription(requestDTO.getDescription());

        if ("INACTIVE".equalsIgnoreCase(
                playingArea.getStatus())) {

            playingArea.setStatus("ACTIVE");
        }

        PlayingArea updatedPlayingArea =
                playingAreaRepository.save(playingArea);

        return mapToResponseDTO(updatedPlayingArea);
    }

    @Override
    public void deactivatePlayingArea(
            String playingAreaId) {

        PlayingArea playingArea =
                playingAreaRepository.findById(playingAreaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Playing Area not found"));

        playingArea.setStatus("INACTIVE");

        playingAreaRepository.save(playingArea);
    }
    
    @Override
    public List<PlayingAreaResponseDTO> searchByName(String name) {
        return playingAreaRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private PlayingAreaResponseDTO mapToResponseDTO(
            PlayingArea playingArea) {

        PlayingAreaResponseDTO responseDTO =
                new PlayingAreaResponseDTO();

        responseDTO.setPlayingAreaId(
                playingArea.getPlayingAreaId());

        responseDTO.setFacilityId(
                playingArea.getFacility().getFacilityId());

        responseDTO.setTurfSportId(
                playingArea.getTurfSport().getTurfSportId());

        responseDTO.setName(
                playingArea.getName());

        responseDTO.setDescription(
                playingArea.getDescription());

        responseDTO.setStatus(
                playingArea.getStatus());

        return responseDTO;
    }
}