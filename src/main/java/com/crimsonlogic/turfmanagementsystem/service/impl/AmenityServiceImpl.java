package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AmenityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Amenity;
import com.crimsonlogic.turfmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.turfmanagementsystem.repository.AmenityRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAmenityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AmenityServiceImpl implements IAmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityServiceImpl(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public AmenityResponseDTO createAmenity(AmenityRequestDTO requestDTO) {

        if (amenityRepository.existsByNameIgnoreCase(requestDTO.getName())) {
            throw new ResourceNotFoundException("Amenity with this name already exists");
        }

        Amenity amenity = new Amenity();
        amenity.setName(requestDTO.getName());
        amenity.setDescription(requestDTO.getDescription());
        amenity.setStatus("ACTIVE");

        Amenity savedAmenity = amenityRepository.save(amenity);

        return mapToResponseDTO(savedAmenity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityResponseDTO> getAllAmenities() {

        return amenityRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AmenityResponseDTO getAmenityById(String amenityId) {

        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));

        return mapToResponseDTO(amenity);
    }

    @Override
    @Transactional(readOnly = true)
    public AmenityResponseDTO getAmenityByName(String name) {

        Amenity amenity = amenityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));

        return mapToResponseDTO(amenity);
    }

    @Override
    public AmenityResponseDTO updateAmenity(
            String amenityId,
            AmenityRequestDTO requestDTO) {

        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));

        if (!amenity.getName().equalsIgnoreCase(requestDTO.getName())
                && amenityRepository.existsByNameIgnoreCase(requestDTO.getName())) {

            throw new IllegalArgumentException(
                    "Amenity with this name already exists"
            );
        }

        amenity.setName(requestDTO.getName());
        amenity.setDescription(requestDTO.getDescription());

        Amenity updatedAmenity = amenityRepository.save(amenity);

        return mapToResponseDTO(updatedAmenity);
    }

    @Override
    public void deactivateAmenity(String amenityId) {

        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));

        amenity.setStatus("INACTIVE");

        amenityRepository.save(amenity);
    }

    private AmenityResponseDTO mapToResponseDTO(Amenity amenity) {

        AmenityResponseDTO responseDTO = new AmenityResponseDTO();

        responseDTO.setAmenityId(amenity.getAmenityId());
        responseDTO.setName(amenity.getName());
        responseDTO.setDescription(amenity.getDescription());
        responseDTO.setStatus(amenity.getStatus());

        return responseDTO;
    }
}