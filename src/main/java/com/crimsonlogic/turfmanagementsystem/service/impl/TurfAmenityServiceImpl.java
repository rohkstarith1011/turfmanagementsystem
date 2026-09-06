package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfAmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfAmenityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Amenity;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.TurfAmenity;
import com.crimsonlogic.turfmanagementsystem.repository.AmenityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfAmenityRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfAmenityService;

@Service
public class TurfAmenityServiceImpl implements ITurfAmenityService {

    private final TurfAmenityRepository turfAmenityRepository;
    private final FacilityRepository facilityRepository;
    private final AmenityRepository amenityRepository;

    public TurfAmenityServiceImpl(
            TurfAmenityRepository turfAmenityRepository,
            FacilityRepository facilityRepository,
            AmenityRepository amenityRepository) {

        this.turfAmenityRepository = turfAmenityRepository;
        this.facilityRepository = facilityRepository;
        this.amenityRepository = amenityRepository;
    }

    @Override
    public TurfAmenityResponseDTO createTurfAmenity(
            TurfAmenityRequestDTO requestDTO) {

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign amenity to an inactive Facility");
        }

        Amenity amenity = amenityRepository
                .findById(requestDTO.getAmenityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Amenity not found"));

        if (!"ACTIVE".equalsIgnoreCase(amenity.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive Amenity to a Facility");
        }

        if (turfAmenityRepository
                .existsByFacilityFacilityIdAndAmenityAmenityId(
                        requestDTO.getFacilityId(),
                        requestDTO.getAmenityId())) {

            throw new IllegalArgumentException(
                    "This Amenity is already assigned to this Facility");
        }

        TurfAmenity turfAmenity = new TurfAmenity();

        turfAmenity.setFacility(facility);
        turfAmenity.setAmenity(amenity);
        turfAmenity.setStatus("ACTIVE");

        TurfAmenity savedTurfAmenity =
                turfAmenityRepository.save(turfAmenity);

        return mapToResponseDTO(savedTurfAmenity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfAmenityResponseDTO> getAllTurfAmenities() {

        return turfAmenityRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TurfAmenityResponseDTO getTurfAmenityById(
            String turfAmenityId) {

        TurfAmenity turfAmenity =
                turfAmenityRepository.findById(turfAmenityId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Amenity not found"));

        return mapToResponseDTO(turfAmenity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfAmenityResponseDTO> getTurfAmenitiesByFacilityId(
            String facilityId) {

        return turfAmenityRepository
                .findByFacilityFacilityId(facilityId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfAmenityResponseDTO> getTurfAmenitiesByAmenityId(
            String amenityId) {

        return turfAmenityRepository
                .findByAmenityAmenityId(amenityId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public TurfAmenityResponseDTO updateTurfAmenity(
            String turfAmenityId,
            TurfAmenityRequestDTO requestDTO) {

        TurfAmenity turfAmenity =
                turfAmenityRepository.findById(turfAmenityId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Amenity not found"));

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        Amenity amenity = amenityRepository
                .findById(requestDTO.getAmenityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Amenity not found"));

        if (!turfAmenity.getFacility().getFacilityId()
                .equals(requestDTO.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Turf Amenity cannot be reassigned to another Facility");
        }

        if (!turfAmenity.getAmenity().getAmenityId()
                .equals(requestDTO.getAmenityId())) {

            throw new IllegalArgumentException(
                    "Turf Amenity cannot be reassigned to another Amenity");
        }

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Turf Amenity for an inactive Facility");
        }

        if (!"ACTIVE".equalsIgnoreCase(amenity.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Turf Amenity with an inactive Amenity");
        }

        turfAmenity.setFacility(facility);
        turfAmenity.setAmenity(amenity);

        if ("INACTIVE".equalsIgnoreCase(turfAmenity.getStatus())) {
            turfAmenity.setStatus("ACTIVE");
        }

        TurfAmenity updatedTurfAmenity =
                turfAmenityRepository.save(turfAmenity);

        return mapToResponseDTO(updatedTurfAmenity);
    }

    @Override
    public void deactivateTurfAmenity(
            String turfAmenityId) {

        TurfAmenity turfAmenity =
                turfAmenityRepository.findById(turfAmenityId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Amenity not found"));

        turfAmenity.setStatus("INACTIVE");

        turfAmenityRepository.save(turfAmenity);
    }

    private TurfAmenityResponseDTO mapToResponseDTO(
            TurfAmenity turfAmenity) {

        TurfAmenityResponseDTO responseDTO =
                new TurfAmenityResponseDTO();

        responseDTO.setTurfAmenityId(
                turfAmenity.getTurfAmenityId());

        responseDTO.setFacilityId(
                turfAmenity.getFacility().getFacilityId());

        responseDTO.setAmenityId(
                turfAmenity.getAmenity().getAmenityId());

        responseDTO.setStatus(
                turfAmenity.getStatus());

        return responseDTO;
    }
}