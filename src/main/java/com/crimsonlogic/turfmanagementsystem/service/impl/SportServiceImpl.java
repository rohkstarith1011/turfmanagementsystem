package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Sport;
import com.crimsonlogic.turfmanagementsystem.repository.SportRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SportServiceImpl implements ISportService {

    private final SportRepository sportRepository;

    public SportServiceImpl(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Override
    public SportResponseDTO createSport(SportRequestDTO requestDTO) {

        if (sportRepository.existsByNameIgnoreCase(requestDTO.getName())) {
            throw new IllegalArgumentException("Sport with this name already exists");
        }

        Sport sport = new Sport();
        sport.setName(requestDTO.getName());
        sport.setDescription(requestDTO.getDescription());
        sport.setStatus("ACTIVE");

        Sport savedSport = sportRepository.save(sport);

        return mapToResponseDTO(savedSport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SportResponseDTO> getAllSports() {

        return sportRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SportResponseDTO getSportById(String sportId) {

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException("Sport not found"));

        return mapToResponseDTO(sport);
    }

    @Override
    @Transactional(readOnly = true)
    public SportResponseDTO getSportByName(String name) {

        Sport sport = sportRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new IllegalArgumentException("Sport not found"));

        return mapToResponseDTO(sport);
    }

    @Override
    public SportResponseDTO updateSport(
            String sportId,
            SportRequestDTO requestDTO) {

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sport not found"));

        if (!sport.getName().equalsIgnoreCase(requestDTO.getName())
                && sportRepository.existsByNameIgnoreCase(
                        requestDTO.getName())) {

            throw new IllegalArgumentException(
                    "Sport with this name already exists");
        }

        sport.setName(requestDTO.getName());
        sport.setDescription(requestDTO.getDescription());

        // Reactivate an existing master sport
        if ("INACTIVE".equalsIgnoreCase(sport.getStatus())) {
            sport.setStatus("ACTIVE");
        }

        Sport updatedSport = sportRepository.save(sport);

        return mapToResponseDTO(updatedSport);
    }

    @Override
    public void deactivateSport(String sportId) {

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException("Sport not found"));

        sport.setStatus("INACTIVE");

        sportRepository.save(sport);
    }

    private SportResponseDTO mapToResponseDTO(Sport sport) {

        SportResponseDTO responseDTO = new SportResponseDTO();

        responseDTO.setSportId(sport.getSportId());
        responseDTO.setName(sport.getName());
        responseDTO.setDescription(sport.getDescription());
        responseDTO.setStatus(sport.getStatus());

        return responseDTO;
    }
}