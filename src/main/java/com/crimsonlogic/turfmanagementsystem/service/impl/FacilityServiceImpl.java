package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.FacilityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.TurfManager;
import com.crimsonlogic.turfmanagementsystem.entity.TurfOwner;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfManagerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfOwnerRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IFacilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FacilityServiceImpl implements IFacilityService {

    private final FacilityRepository facilityRepository;
    private final TurfOwnerRepository turfOwnerRepository;
    private final TurfManagerRepository turfManagerRepository;

    public FacilityServiceImpl(FacilityRepository facilityRepository,
                               TurfOwnerRepository turfOwnerRepository,
                               TurfManagerRepository turfManagerRepository) {
        this.facilityRepository = facilityRepository;
        this.turfOwnerRepository = turfOwnerRepository;
        this.turfManagerRepository = turfManagerRepository;
    }

    @Override
    public FacilityResponseDTO createFacility(FacilityRequestDTO requestDTO) {

        validateOperatingHours(requestDTO);

        TurfOwner owner = turfOwnerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Turf Owner not found"));

        if (owner.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot create facility for an inactive Turf Owner");
        }

        TurfManager manager = turfManagerRepository.findById(requestDTO.getManagerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Turf Manager not found"));

        if (manager.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot create facility with an inactive Turf Manager");
        }

        if (facilityRepository.existsByOwnerTurfOwnerId(owner.getTurfOwnerId())) {
            throw new IllegalArgumentException(
                    "Turf Owner is already assigned to a facility");
        }

        if (facilityRepository.existsByManagerTurfManagerId(manager.getTurfManagerId())) {
            throw new IllegalArgumentException(
                    "Turf Manager is already assigned to a facility");
        }

        Facility facility = new Facility();

        facility.setName(requestDTO.getName());
        facility.setOwner(owner);
        facility.setManager(manager);
        facility.setLocation(requestDTO.getLocation());
        facility.setAddress(requestDTO.getAddress());
        facility.setLocality(requestDTO.getLocality());
        facility.setCity(requestDTO.getCity());
        facility.setState(requestDTO.getState());
       
        facility.setCapacity(requestDTO.getCapacity());
        facility.setOpeningTime(requestDTO.getOpeningTime());
        facility.setClosingTime(requestDTO.getClosingTime());
        facility.setBasePrice(requestDTO.getBasePrice());
        facility.setRules(requestDTO.getRules());

        facility.setRating(0.0);
        facility.setAvailability(true);
        facility.setStatus("ACTIVE");

        Facility savedFacility = facilityRepository.save(facility);

        return mapToResponseDTO(savedFacility);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getAllFacilities() {

        return facilityRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FacilityResponseDTO getFacilityById(String facilityId) {

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Facility not found"));

        return mapToResponseDTO(facility);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByName(String name) {

        return facilityRepository.findByNameIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByCity(String city) {

        return facilityRepository.findByCityIgnoreCase(city)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByState(String state) {

        return facilityRepository.findByStateIgnoreCase(state)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByLocality(String locality) {

        return facilityRepository.findByLocalityIgnoreCase(locality)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

  
    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByOwnerId(String ownerId) {

        return facilityRepository.findByOwnerTurfOwnerId(ownerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByManagerId(String managerId) {

        return facilityRepository.findByManagerTurfManagerId(managerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public FacilityResponseDTO updateFacility(
            String facilityId,
            FacilityRequestDTO requestDTO) {

        validateOperatingHours(requestDTO);

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Facility not found"));

        TurfOwner owner = turfOwnerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Turf Owner not found"));

        if (owner.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive Turf Owner");
        }

        TurfManager manager = turfManagerRepository.findById(requestDTO.getManagerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Turf Manager not found"));

        if (manager.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive Turf Manager");
        }

        boolean ownerChanged =
                !facility.getOwner().getTurfOwnerId()
                        .equals(owner.getTurfOwnerId());

        boolean managerChanged =
                !facility.getManager().getTurfManagerId()
                        .equals(manager.getTurfManagerId());

        if (ownerChanged &&
                facilityRepository.existsByOwnerTurfOwnerId(owner.getTurfOwnerId())) {

            throw new IllegalArgumentException(
                    "Turf Owner is already assigned to another facility");
        }

        if (managerChanged &&
                facilityRepository.existsByManagerTurfManagerId(manager.getTurfManagerId())) {

            throw new IllegalArgumentException(
                    "Turf Manager is already assigned to another facility");
        }

        facility.setName(requestDTO.getName());
        facility.setOwner(owner);
        facility.setManager(manager);
        facility.setLocation(requestDTO.getLocation());
        facility.setAddress(requestDTO.getAddress());
        facility.setLocality(requestDTO.getLocality());
        facility.setCity(requestDTO.getCity());
        facility.setState(requestDTO.getState());
       
        facility.setCapacity(requestDTO.getCapacity());
        facility.setOpeningTime(requestDTO.getOpeningTime());
        facility.setClosingTime(requestDTO.getClosingTime());
        facility.setBasePrice(requestDTO.getBasePrice());
        facility.setRules(requestDTO.getRules());

        Facility updatedFacility = facilityRepository.save(facility);

        return mapToResponseDTO(updatedFacility);
    }

    @Override
    public void deactivateFacility(String facilityId) {

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Facility not found"));

        facility.setStatus("INACTIVE");
        facility.setAvailability(false);

        facilityRepository.save(facility);
    }

    private void validateOperatingHours(FacilityRequestDTO requestDTO) {

        if (!requestDTO.getOpeningTime()
                .isBefore(requestDTO.getClosingTime())) {

            throw new IllegalArgumentException(
                    "Opening time must be before closing time");
        }
    }

    private FacilityResponseDTO mapToResponseDTO(Facility facility) {

        FacilityResponseDTO responseDTO = new FacilityResponseDTO();

        responseDTO.setFacilityId(facility.getFacilityId());
        responseDTO.setName(facility.getName());

        responseDTO.setOwnerId(
                facility.getOwner().getTurfOwnerId());

        responseDTO.setManagerId(
                facility.getManager().getTurfManagerId());

        responseDTO.setLocation(facility.getLocation());
        responseDTO.setAddress(facility.getAddress());
        responseDTO.setLocality(facility.getLocality());
        responseDTO.setCity(facility.getCity());
        responseDTO.setState(facility.getState());
      
        responseDTO.setCapacity(facility.getCapacity());
        responseDTO.setOpeningTime(facility.getOpeningTime());
        responseDTO.setClosingTime(facility.getClosingTime());
        responseDTO.setBasePrice(facility.getBasePrice());
        responseDTO.setRules(facility.getRules());
        responseDTO.setRating(facility.getRating());
        responseDTO.setAvailability(facility.getAvailability());
        responseDTO.setStatus(facility.getStatus());

        return responseDTO;
    }
}