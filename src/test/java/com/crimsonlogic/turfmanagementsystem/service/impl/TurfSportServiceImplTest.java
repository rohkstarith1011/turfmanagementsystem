package com.crimsonlogic.turfmanagementsystem.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfSportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfSportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.Sport;
import com.crimsonlogic.turfmanagementsystem.entity.TurfSport;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SportRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfSportRepository;

@ExtendWith(MockitoExtension.class)
class TurfSportServiceImplTest {

    @Mock
    private TurfSportRepository turfSportRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private TurfSportServiceImpl turfSportService;

    private Facility facility;
    private Facility anotherFacility;

    private Sport sport;
    private Sport anotherSport;

    private TurfSport turfSport;
    private TurfSportRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        facility = new Facility();
        facility.setFacilityId("FAC123456");
        facility.setName("City Turf");
        facility.setStatus("ACTIVE");

        anotherFacility = new Facility();
        anotherFacility.setFacilityId("FAC654321");
        anotherFacility.setName("Another Turf");
        anotherFacility.setStatus("ACTIVE");

        sport = new Sport();
        sport.setSportId("SPR123456");
        sport.setName("Football");
        sport.setStatus("ACTIVE");

        anotherSport = new Sport();
        anotherSport.setSportId("SPR654321");
        anotherSport.setName("Cricket");
        anotherSport.setStatus("ACTIVE");

        turfSport = new TurfSport();
        turfSport.setTurfSportId("TFSPR123456");
        turfSport.setFacility(facility);
        turfSport.setSport(sport);
        turfSport.setStatus("ACTIVE");

        requestDTO = new TurfSportRequestDTO();
        requestDTO.setFacilityId("FAC123456");
        requestDTO.setSportId("SPR123456");
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void createTurfSport_success() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        when(turfSportRepository
                .existsByFacilityFacilityIdAndSportSportId(
                        "FAC123456", "SPR123456"))
                .thenReturn(false);

        when(turfSportRepository.save(any(TurfSport.class)))
                .thenReturn(turfSport);

        TurfSportResponseDTO response =
                turfSportService.createTurfSport(requestDTO);

        assertNotNull(response);
        assertEquals("TFSPR123456", response.getTurfSportId());
        assertEquals("FAC123456", response.getFacilityId());
        assertEquals("SPR123456", response.getSportId());
        assertEquals("ACTIVE", response.getStatus());

        verify(facilityRepository).findById("FAC123456");
        verify(sportRepository).findById("SPR123456");
        verify(turfSportRepository)
                .existsByFacilityFacilityIdAndSportSportId(
                        "FAC123456", "SPR123456");
        verify(turfSportRepository).save(any(TurfSport.class));
    }

    @Test
    void createTurfSport_facilityNotFound() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.createTurfSport(requestDTO));

        assertEquals("Facility not found", exception.getMessage());

        verify(facilityRepository).findById("FAC123456");
        verifyNoInteractions(sportRepository);
        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void createTurfSport_inactiveFacility() {

        facility.setStatus("INACTIVE");

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.createTurfSport(requestDTO));

        assertEquals(
                "Cannot assign sport to an inactive Facility",
                exception.getMessage());

        verifyNoInteractions(sportRepository);
        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void createTurfSport_sportNotFound() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.createTurfSport(requestDTO));

        assertEquals("Sport not found", exception.getMessage());

        verify(sportRepository).findById("SPR123456");
        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void createTurfSport_inactiveSport() {

        sport.setStatus("INACTIVE");

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.createTurfSport(requestDTO));

        assertEquals(
                "Cannot assign an inactive Sport to a Facility",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void createTurfSport_duplicateMapping() {

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        when(turfSportRepository
                .existsByFacilityFacilityIdAndSportSportId(
                        "FAC123456", "SPR123456"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.createTurfSport(requestDTO));

        assertEquals(
                "This Sport is already assigned to this Facility",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAllTurfSports_success() {

        TurfSport secondTurfSport = new TurfSport();
        secondTurfSport.setTurfSportId("TFSPR654321");
        secondTurfSport.setFacility(facility);
        secondTurfSport.setSport(anotherSport);
        secondTurfSport.setStatus("ACTIVE");

        when(turfSportRepository.findAll())
                .thenReturn(Arrays.asList(turfSport, secondTurfSport));

        List<TurfSportResponseDTO> response =
                turfSportService.getAllTurfSports();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(
                "TFSPR123456",
                response.get(0).getTurfSportId());

        assertEquals(
                "TFSPR654321",
                response.get(1).getTurfSportId());

        verify(turfSportRepository).findAll();
    }

    @Test
    void getAllTurfSports_empty() {

        when(turfSportRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<TurfSportResponseDTO> response =
                turfSportService.getAllTurfSports();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(turfSportRepository).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getTurfSportById_success() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        TurfSportResponseDTO response =
                turfSportService.getTurfSportById("TFSPR123456");

        assertNotNull(response);
        assertEquals(
                "TFSPR123456",
                response.getTurfSportId());

        assertEquals(
                "FAC123456",
                response.getFacilityId());

        assertEquals(
                "SPR123456",
                response.getSportId());

        verify(turfSportRepository)
                .findById("TFSPR123456");
    }

    @Test
    void getTurfSportById_notFound() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService
                                .getTurfSportById("TFSPR123456"));

        assertEquals(
                "Turf Sport not found",
                exception.getMessage());
    }

    // ---------------------------------------------------------
    // GET BY FACILITY ID
    // ---------------------------------------------------------

    @Test
    void getTurfSportsByFacilityId_success() {

        when(turfSportRepository
                .findByFacilityFacilityId("FAC123456"))
                .thenReturn(List.of(turfSport));

        List<TurfSportResponseDTO> response =
                turfSportService
                        .getTurfSportsByFacilityId("FAC123456");

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(
                "FAC123456",
                response.get(0).getFacilityId());

        verify(turfSportRepository)
                .findByFacilityFacilityId("FAC123456");
    }

    // ---------------------------------------------------------
    // GET BY SPORT ID
    // ---------------------------------------------------------

    @Test
    void getTurfSportsBySportId_success() {

        when(turfSportRepository
                .findBySportSportId("SPR123456"))
                .thenReturn(List.of(turfSport));

        List<TurfSportResponseDTO> response =
                turfSportService
                        .getTurfSportsBySportId("SPR123456");

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(
                "SPR123456",
                response.get(0).getSportId());

        verify(turfSportRepository)
                .findBySportSportId("SPR123456");
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateTurfSport_success() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        when(turfSportRepository.save(any(TurfSport.class)))
                .thenReturn(turfSport);

        TurfSportResponseDTO response =
                turfSportService.updateTurfSport(
                        "TFSPR123456",
                        requestDTO);

        assertNotNull(response);
        assertEquals(
                "TFSPR123456",
                response.getTurfSportId());

        assertEquals(
                "FAC123456",
                response.getFacilityId());

        assertEquals(
                "SPR123456",
                response.getSportId());

        verify(turfSportRepository)
                .save(turfSport);
    }

    @Test
    void updateTurfSport_notFound() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Turf Sport not found",
                exception.getMessage());
    }

    @Test
    void updateTurfSport_facilityNotFound() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Facility not found",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void updateTurfSport_sportNotFound() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Sport not found",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void updateTurfSport_facilityReassignmentNotAllowed() {

        requestDTO.setFacilityId("FAC654321");

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC654321"))
                .thenReturn(Optional.of(anotherFacility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Turf Sport cannot be reassigned to another Facility",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void updateTurfSport_sportReassignmentNotAllowed() {

        requestDTO.setSportId("SPR654321");

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR654321"))
                .thenReturn(Optional.of(anotherSport));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Turf Sport cannot be reassigned to another Sport",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void updateTurfSport_inactiveFacility() {

        facility.setStatus("INACTIVE");

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Cannot update Turf Sport for an inactive Facility",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    @Test
    void updateTurfSport_inactiveSport() {

        sport.setStatus("INACTIVE");

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        when(facilityRepository.findById("FAC123456"))
                .thenReturn(Optional.of(facility));

        when(sportRepository.findById("SPR123456"))
                .thenReturn(Optional.of(sport));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService.updateTurfSport(
                                "TFSPR123456",
                                requestDTO));

        assertEquals(
                "Cannot update Turf Sport with an inactive Sport",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }

    // ---------------------------------------------------------
    // DEACTIVATE
    // ---------------------------------------------------------

    @Test
    void deactivateTurfSport_success() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.of(turfSport));

        turfSportService.deactivateTurfSport("TFSPR123456");

        assertEquals("INACTIVE", turfSport.getStatus());

        verify(turfSportRepository)
                .findById("TFSPR123456");

        verify(turfSportRepository)
                .save(turfSport);
    }

    @Test
    void deactivateTurfSport_notFound() {

        when(turfSportRepository.findById("TFSPR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfSportService
                                .deactivateTurfSport("TFSPR123456"));

        assertEquals(
                "Turf Sport not found",
                exception.getMessage());

        verify(turfSportRepository, never()).save(any());
    }
}