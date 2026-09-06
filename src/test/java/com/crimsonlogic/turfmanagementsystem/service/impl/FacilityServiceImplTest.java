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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityServiceImplTest {

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private TurfOwnerRepository turfOwnerRepository;

    @Mock
    private TurfManagerRepository turfManagerRepository;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    private FacilityRequestDTO requestDTO;
    private TurfOwner owner;
    private TurfManager manager;
    private Facility facility;

    @BeforeEach
    void setUp() {

        owner = new TurfOwner();
        owner.setTurfOwnerId("TFO123456");
        owner.setStatus(UserStatus.ACTIVE);

        manager = new TurfManager();
        manager.setTurfManagerId("TFM123456");
        manager.setStatus(UserStatus.ACTIVE);

        requestDTO = new FacilityRequestDTO();

        requestDTO.setName("Champions Turf");
        requestDTO.setOwnerId("TFO123456");
        requestDTO.setManagerId("TFM123456");
        requestDTO.setLocation("Yelahanka");
        requestDTO.setAddress("Main Road");
        requestDTO.setLocality("New Town");
        requestDTO.setCity("Bangalore");
        requestDTO.setState("Karnataka");
    
        requestDTO.setCapacity(20);
        requestDTO.setOpeningTime(LocalTime.of(6, 0));
        requestDTO.setClosingTime(LocalTime.of(22, 0));
        requestDTO.setBasePrice(1500.0);
        requestDTO.setRules("No smoking. Proper sports shoes required.");

        facility = new Facility();

        facility.setFacilityId("FAC123456");
        facility.setName("Champions Turf");
        facility.setOwner(owner);
        facility.setManager(manager);
        facility.setLocation("Yelahanka");
        facility.setAddress("Main Road");
        facility.setLocality("New Town");
        facility.setCity("Bangalore");
        facility.setState("Karnataka");
 
        facility.setCapacity(20);
        facility.setOpeningTime(LocalTime.of(6, 0));
        facility.setClosingTime(LocalTime.of(22, 0));
        facility.setBasePrice(1500.0);
        facility.setRules("No smoking. Proper sports shoes required.");
        facility.setRating(0.0);
        facility.setAvailability(true);
        facility.setStatus("ACTIVE");
    }

    // ---------------------------------------------------------
    // CREATE FACILITY
    // ---------------------------------------------------------

    @Test
    void createFacility_shouldCreateSuccessfully() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        when(facilityRepository.existsByOwnerTurfOwnerId("TFO123456"))
                .thenReturn(false);

        when(facilityRepository.existsByManagerTurfManagerId("TFM123456"))
                .thenReturn(false);

        when(facilityRepository.save(any(Facility.class)))
                .thenReturn(facility);

        FacilityResponseDTO response =
                facilityService.createFacility(requestDTO);

        assertNotNull(response);
        assertEquals("FAC123456", response.getFacilityId());
        assertEquals("Champions Turf", response.getName());
        assertEquals("TFO123456", response.getOwnerId());
        assertEquals("TFM123456", response.getManagerId());
        assertEquals("Bangalore", response.getCity());
        assertEquals("ACTIVE", response.getStatus());
        assertTrue(response.getAvailability());

        verify(facilityRepository).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenOwnerNotFound() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals("Turf Owner not found", exception.getMessage());

        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenOwnerInactive() {

        owner.setStatus(UserStatus.INACTIVE);

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals(
                "Cannot create facility for an inactive Turf Owner",
                exception.getMessage()
        );

        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenManagerNotFound() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals("Turf Manager not found", exception.getMessage());

        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenManagerInactive() {

        manager.setStatus(UserStatus.INACTIVE);

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals(
                "Cannot create facility with an inactive Turf Manager",
                exception.getMessage()
        );

        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenOwnerAlreadyAssigned() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        when(facilityRepository.existsByOwnerTurfOwnerId("TFO123456"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals(
                "Turf Owner is already assigned to a facility",
                exception.getMessage()
        );

        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenManagerAlreadyAssigned() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        when(facilityRepository.existsByOwnerTurfOwnerId("TFO123456"))
                .thenReturn(false);

        when(facilityRepository.existsByManagerTurfManagerId("TFM123456"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals(
                "Turf Manager is already assigned to a facility",
                exception.getMessage()
        );

        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createFacility_shouldThrowExceptionWhenOpeningTimeIsAfterClosingTime() {

        requestDTO.setOpeningTime(LocalTime.of(22, 0));
        requestDTO.setClosingTime(LocalTime.of(6, 0));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.createFacility(requestDTO)
                );

        assertEquals(
                "Opening time must be before closing time",
                exception.getMessage()
        );

        verify(turfOwnerRepository, never()).findById(anyString());
        verify(facilityRepository, never()).save(any(Facility.class));
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAllFacilities_shouldReturnFacilities() {

        Facility secondFacility = new Facility();
        secondFacility.setFacilityId("FAC654321");
        secondFacility.setName("Elite Arena");
        secondFacility.setOwner(owner);
        secondFacility.setManager(manager);
        secondFacility.setLocation("Hebbal");
        secondFacility.setAddress("Airport Road");
        secondFacility.setLocality("Hebbal");
        secondFacility.setCity("Bangalore");
        secondFacility.setState("Karnataka");

        secondFacility.setCapacity(25);
        secondFacility.setOpeningTime(LocalTime.of(6, 0));
        secondFacility.setClosingTime(LocalTime.of(22, 0));
        secondFacility.setBasePrice(1800.0);
        secondFacility.setRules("No smoking");
        secondFacility.setRating(4.5);
        secondFacility.setAvailability(true);
        secondFacility.setStatus("ACTIVE");

        when(facilityRepository.findAll())
                .thenReturn(Arrays.asList(facility, secondFacility));

        List<FacilityResponseDTO> result =
                facilityService.getAllFacilities();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Champions Turf", result.get(0).getName());
        assertEquals("Elite Arena", result.get(1).getName());

        verify(facilityRepository).findAll();
    }

    @Test
    void getAllFacilities_shouldReturnEmptyListWhenNoFacilitiesExist() {

        when(facilityRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<FacilityResponseDTO> result =
                facilityService.getAllFacilities();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(facilityRepository).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getFacilityById_shouldReturnFacility() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        FacilityResponseDTO result =
                facilityService.getFacilityById("FAC123456");

        assertNotNull(result);
        assertEquals("FAC123456", result.getFacilityId());
        assertEquals("Champions Turf", result.getName());
        assertEquals("TFO123456", result.getOwnerId());
        assertEquals("TFM123456", result.getManagerId());

        verify(facilityRepository).findById("FAC123456");
    }

    @Test
    void getFacilityById_shouldThrowExceptionWhenFacilityNotFound() {

        when(facilityRepository.findById("FAC999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.getFacilityById("FAC999999")
                );

        assertEquals("Facility not found", exception.getMessage());
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByName_shouldReturnFacilities() {

        when(facilityRepository.findByNameIgnoreCase("Champions Turf"))
                .thenReturn(List.of(facility));

        List<FacilityResponseDTO> result =
                facilityService.getFacilitiesByName("Champions Turf");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Champions Turf", result.get(0).getName());

        verify(facilityRepository)
                .findByNameIgnoreCase("Champions Turf");
    }

    // ---------------------------------------------------------
    // GET BY CITY
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByCity_shouldReturnFacilities() {

        when(facilityRepository.findByCityIgnoreCase("Bangalore"))
                .thenReturn(List.of(facility));

        List<FacilityResponseDTO> result =
                facilityService.getFacilitiesByCity("Bangalore");

        assertEquals(1, result.size());
        assertEquals("Bangalore", result.get(0).getCity());

        verify(facilityRepository)
                .findByCityIgnoreCase("Bangalore");
    }

    // ---------------------------------------------------------
    // GET BY STATE
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByState_shouldReturnFacilities() {

        when(facilityRepository.findByStateIgnoreCase("Karnataka"))
                .thenReturn(List.of(facility));

        List<FacilityResponseDTO> result =
                facilityService.getFacilitiesByState("Karnataka");

        assertEquals(1, result.size());
        assertEquals("Karnataka", result.get(0).getState());

        verify(facilityRepository)
                .findByStateIgnoreCase("Karnataka");
    }

    // ---------------------------------------------------------
    // GET BY LOCALITY
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByLocality_shouldReturnFacilities() {

        when(facilityRepository.findByLocalityIgnoreCase("New Town"))
                .thenReturn(List.of(facility));

        List<FacilityResponseDTO> result =
                facilityService.getFacilitiesByLocality("New Town");

        assertEquals(1, result.size());
        assertEquals("New Town", result.get(0).getLocality());

        verify(facilityRepository)
                .findByLocalityIgnoreCase("New Town");
    }

    // ---------------------------------------------------------
    // GET BY TURF TYPE
    // ---------------------------------------------------------

    

    // ---------------------------------------------------------
    // GET BY OWNER
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByOwnerId_shouldReturnFacilities() {

        when(facilityRepository.findByOwnerTurfOwnerId("TFO123456"))
                .thenReturn(List.of(facility));

        List<FacilityResponseDTO> result =
                facilityService.getFacilitiesByOwnerId("TFO123456");

        assertEquals(1, result.size());
        assertEquals("TFO123456", result.get(0).getOwnerId());

        verify(facilityRepository)
                .findByOwnerTurfOwnerId("TFO123456");
    }

    // ---------------------------------------------------------
    // GET BY MANAGER
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByManagerId_shouldReturnFacilities() {

        when(facilityRepository.findByManagerTurfManagerId("TFM123456"))
                .thenReturn(List.of(facility));

        List<FacilityResponseDTO> result =
                facilityService.getFacilitiesByManagerId("TFM123456");

        assertEquals(1, result.size());
        assertEquals("TFM123456", result.get(0).getManagerId());

        verify(facilityRepository)
                .findByManagerTurfManagerId("TFM123456");
    }

    // ---------------------------------------------------------
    // UPDATE FACILITY
    // ---------------------------------------------------------

    @Test
    void updateFacility_shouldUpdateSuccessfully() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        when(facilityRepository.save(any(Facility.class)))
                .thenReturn(facility);

        requestDTO.setName("Updated Champions Turf");
        requestDTO.setBasePrice(1800.0);

        FacilityResponseDTO result =
                facilityService.updateFacility(
                        "FAC123456",
                        requestDTO
                );

        assertNotNull(result);
        assertEquals("Updated Champions Turf", facility.getName());
        assertEquals(1800.0, facility.getBasePrice());

        verify(facilityRepository).save(facility);
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenFacilityNotFound() {

        when(facilityRepository.findById("FAC999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC999999",
                                requestDTO
                        )
                );

        assertEquals("Facility not found", exception.getMessage());
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenOwnerNotFound() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals("Turf Owner not found", exception.getMessage());
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenManagerNotFound() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals("Turf Manager not found", exception.getMessage());
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenOwnerAlreadyAssignedToAnotherFacility() {

        TurfOwner anotherOwner = new TurfOwner();
        anotherOwner.setTurfOwnerId("TFO654321");
        anotherOwner.setStatus(UserStatus.ACTIVE);

        requestDTO.setOwnerId("TFO654321");

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO654321"))
                .thenReturn(Optional.of(anotherOwner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        when(facilityRepository.existsByOwnerTurfOwnerId("TFO654321"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Turf Owner is already assigned to another facility",
                exception.getMessage()
        );

        verify(facilityRepository, never())
                .save(any(Facility.class));
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenManagerAlreadyAssignedToAnotherFacility() {

        TurfManager anotherManager = new TurfManager();
        anotherManager.setTurfManagerId("TFM654321");
        anotherManager.setStatus(UserStatus.ACTIVE);

        requestDTO.setManagerId("TFM654321");

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM654321"))
                .thenReturn(Optional.of(anotherManager));

        when(facilityRepository.existsByManagerTurfManagerId("TFM654321"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Turf Manager is already assigned to another facility",
                exception.getMessage()
        );

        verify(facilityRepository, never())
                .save(any(Facility.class));
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenOwnerInactive() {

        owner.setStatus(UserStatus.INACTIVE);

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Cannot assign an inactive Turf Owner",
                exception.getMessage()
        );

        verify(facilityRepository, never())
                .save(any(Facility.class));
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenManagerInactive() {

        manager.setStatus(UserStatus.INACTIVE);

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(owner));

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(manager));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Cannot assign an inactive Turf Manager",
                exception.getMessage()
        );

        verify(facilityRepository, never())
                .save(any(Facility.class));
    }

    @Test
    void updateFacility_shouldThrowExceptionWhenOperatingHoursInvalid() {

        requestDTO.setOpeningTime(LocalTime.of(22, 0));
        requestDTO.setClosingTime(LocalTime.of(6, 0));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.updateFacility(
                                "FAC123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Opening time must be before closing time",
                exception.getMessage()
        );

        verify(facilityRepository, never())
                .findById(anyString());
    }

    // ---------------------------------------------------------
    // DEACTIVATE FACILITY
    // ---------------------------------------------------------

    @Test
    void deactivateFacility_shouldDeactivateSuccessfully() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(facilityRepository.save(any(Facility.class)))
                .thenReturn(facility);

        facilityService.deactivateFacility("FAC123456");

        assertEquals("INACTIVE", facility.getStatus());
        assertFalse(facility.getAvailability());

        verify(facilityRepository).save(facility);
    }

    @Test
    void deactivateFacility_shouldThrowExceptionWhenFacilityNotFound() {

        when(facilityRepository.findById("FAC999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> facilityService.deactivateFacility("FAC999999")
                );

        assertEquals("Facility not found", exception.getMessage());

        verify(facilityRepository, never())
                .save(any(Facility.class));
    }
}