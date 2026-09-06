package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Sport;
import com.crimsonlogic.turfmanagementsystem.repository.SportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportServiceImplTest {

    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private SportServiceImpl sportService;

    private Sport sport;
    private SportRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        sport = new Sport(
                "SPR001",
                "Football",
                "Football turf sport",
                "ACTIVE"
        );

        requestDTO = new SportRequestDTO();
        requestDTO.setName("Football");
        requestDTO.setDescription("Football turf sport");
    }

    // =========================
    // CREATE SPORT
    // =========================

    @Test
    void createSportSuccessfully() {

        when(sportRepository.existsByNameIgnoreCase("Football"))
                .thenReturn(false);

        when(sportRepository.save(any(Sport.class)))
                .thenReturn(sport);

        SportResponseDTO result =
                sportService.createSport(requestDTO);

        assertNotNull(result);
        assertEquals("SPR001", result.getSportId());
        assertEquals("Football", result.getName());
        assertEquals("Football turf sport", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());

        verify(sportRepository)
                .existsByNameIgnoreCase("Football");

        verify(sportRepository)
                .save(any(Sport.class));
    }

    @Test
    void createSport_DuplicateNameRejected() {

        when(sportRepository.existsByNameIgnoreCase("Football"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sportService.createSport(requestDTO)
                );

        assertEquals(
                "Sport with this name already exists",
                exception.getMessage()
        );

        verify(sportRepository)
                .existsByNameIgnoreCase("Football");

        verify(sportRepository, never())
                .save(any(Sport.class));
    }

    // =========================
    // GET ALL SPORTS
    // =========================

    @Test
    void getAllSportsSuccessfully() {

        Sport football = new Sport(
                "SPR001",
                "Football",
                "Football turf sport",
                "ACTIVE"
        );

        Sport cricket = new Sport(
                "SPR002",
                "Cricket",
                "Cricket turf sport",
                "ACTIVE"
        );

        Sport inactiveSport = new Sport(
                "SPR003",
                "Hockey",
                "Hockey turf sport",
                "INACTIVE"
        );

        when(sportRepository.findAll())
                .thenReturn(List.of(
                        football,
                        cricket,
                        inactiveSport
                ));

        List<SportResponseDTO> result =
                sportService.getAllSports();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals("Football", result.get(0).getName());
        assertEquals("Cricket", result.get(1).getName());
        assertEquals("Hockey", result.get(2).getName());

        assertEquals("ACTIVE", result.get(0).getStatus());
        assertEquals("ACTIVE", result.get(1).getStatus());
        assertEquals("INACTIVE", result.get(2).getStatus());

        verify(sportRepository).findAll();
    }

    @Test
    void getAllSports_WhenNoSportsExist_ReturnsEmptyList() {

        when(sportRepository.findAll())
                .thenReturn(List.of());

        List<SportResponseDTO> result =
                sportService.getAllSports();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(sportRepository).findAll();
    }

    // =========================
    // GET SPORT BY ID
    // =========================

    @Test
    void getSportByIdSuccessfully() {

        when(sportRepository.findById("SPR001"))
                .thenReturn(Optional.of(sport));

        SportResponseDTO result =
                sportService.getSportById("SPR001");

        assertNotNull(result);
        assertEquals("SPR001", result.getSportId());
        assertEquals("Football", result.getName());
        assertEquals("Football turf sport", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());

        verify(sportRepository).findById("SPR001");
    }

    @Test
    void getSportById_SportNotFound() {

        when(sportRepository.findById("SPR999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sportService.getSportById("SPR999")
                );

        assertEquals(
                "Sport not found",
                exception.getMessage()
        );

        verify(sportRepository).findById("SPR999");
    }

    // =========================
    // GET SPORT BY NAME
    // =========================

    @Test
    void getSportByNameSuccessfully() {

        when(sportRepository.findByNameIgnoreCase("football"))
                .thenReturn(Optional.of(sport));

        SportResponseDTO result =
                sportService.getSportByName("football");

        assertNotNull(result);
        assertEquals("SPR001", result.getSportId());
        assertEquals("Football", result.getName());
        assertEquals("Football turf sport", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());

        verify(sportRepository)
                .findByNameIgnoreCase("football");
    }

    @Test
    void getSportByName_SportNotFound() {

        when(sportRepository.findByNameIgnoreCase("Basketball"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sportService.getSportByName("Basketball")
                );

        assertEquals(
                "Sport not found",
                exception.getMessage()
        );

        verify(sportRepository)
                .findByNameIgnoreCase("Basketball");
    }

    // =========================
    // UPDATE SPORT
    // =========================

    @Test
    void updateSportSuccessfully() {

        SportRequestDTO updateRequest = new SportRequestDTO();
        updateRequest.setName("Football 5s");
        updateRequest.setDescription("Updated football description");

        when(sportRepository.findById("SPR001"))
                .thenReturn(Optional.of(sport));

        when(sportRepository.existsByNameIgnoreCase("Football 5s"))
                .thenReturn(false);

        when(sportRepository.save(sport))
                .thenReturn(sport);

        SportResponseDTO result =
                sportService.updateSport(
                        "SPR001",
                        updateRequest
                );

        assertNotNull(result);

        assertEquals("Football 5s", sport.getName());
        assertEquals(
                "Updated football description",
                sport.getDescription()
        );

        assertEquals("SPR001", result.getSportId());
        assertEquals("Football 5s", result.getName());
        assertEquals(
                "Updated football description",
                result.getDescription()
        );
        assertEquals("ACTIVE", result.getStatus());

        verify(sportRepository).findById("SPR001");

        verify(sportRepository)
                .existsByNameIgnoreCase("Football 5s");

        verify(sportRepository)
                .save(sport);
    }

    @Test
    void updateSport_SportNotFound() {

        when(sportRepository.findById("SPR999"))
                .thenReturn(Optional.empty());

        SportRequestDTO updateRequest = new SportRequestDTO();
        updateRequest.setName("Football");
        updateRequest.setDescription("Updated description");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sportService.updateSport(
                                "SPR999",
                                updateRequest
                        )
                );

        assertEquals(
                "Sport not found",
                exception.getMessage()
        );

        verify(sportRepository).findById("SPR999");

        verify(sportRepository, never())
                .save(any(Sport.class));
    }

    @Test
    void updateSport_DuplicateNameRejected() {

        SportRequestDTO updateRequest = new SportRequestDTO();
        updateRequest.setName("Cricket");
        updateRequest.setDescription("Updated description");

        when(sportRepository.findById("SPR001"))
                .thenReturn(Optional.of(sport));

        when(sportRepository.existsByNameIgnoreCase("Cricket"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sportService.updateSport(
                                "SPR001",
                                updateRequest
                        )
                );

        assertEquals(
                "Sport with this name already exists",
                exception.getMessage()
        );

        verify(sportRepository).findById("SPR001");

        verify(sportRepository)
                .existsByNameIgnoreCase("Cricket");

        verify(sportRepository, never())
                .save(any(Sport.class));
    }

    @Test
    void updateSport_SameNameAllowed() {

        SportRequestDTO updateRequest = new SportRequestDTO();
        updateRequest.setName("Football");
        updateRequest.setDescription("Updated football description");

        when(sportRepository.findById("SPR001"))
                .thenReturn(Optional.of(sport));

        when(sportRepository.save(sport))
                .thenReturn(sport);

        SportResponseDTO result =
                sportService.updateSport(
                        "SPR001",
                        updateRequest
                );

        assertNotNull(result);
        assertEquals("Football", result.getName());
        assertEquals(
                "Updated football description",
                result.getDescription()
        );

        verify(sportRepository).findById("SPR001");

        verify(sportRepository, never())
                .existsByNameIgnoreCase("Football");

        verify(sportRepository).save(sport);
    }

    // =========================
    // DEACTIVATE SPORT
    // =========================

    @Test
    void deactivateSportSuccessfully() {

        when(sportRepository.findById("SPR001"))
                .thenReturn(Optional.of(sport));

        when(sportRepository.save(sport))
                .thenReturn(sport);

        sportService.deactivateSport("SPR001");

        assertEquals("INACTIVE", sport.getStatus());

        verify(sportRepository)
                .findById("SPR001");

        verify(sportRepository)
                .save(sport);
    }

    @Test
    void deactivateSport_SportNotFound() {

        when(sportRepository.findById("SPR999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sportService.deactivateSport("SPR999")
                );

        assertEquals(
                "Sport not found",
                exception.getMessage()
        );

        verify(sportRepository)
                .findById("SPR999");

        verify(sportRepository, never())
                .save(any(Sport.class));
    }
}