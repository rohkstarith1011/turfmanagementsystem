package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportControllerTest {

    @Mock
    private ISportService sportService;

    @InjectMocks
    private SportController sportController;

    private SportRequestDTO requestDTO;
    private SportResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new SportRequestDTO();
        requestDTO.setName("Football");
        requestDTO.setDescription("Football turf sport");

        responseDTO = new SportResponseDTO();
        responseDTO.setSportId("SPR001");
        responseDTO.setName("Football");
        responseDTO.setDescription("Football turf sport");
        responseDTO.setStatus("ACTIVE");
    }

    @Test
    void createSportSuccessfully() {

        when(sportService.createSport(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<SportResponseDTO> response =
                sportController.createSport(requestDTO);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("SPR001", response.getBody().getSportId());
        assertEquals("Football", response.getBody().getName());
        assertEquals("Football turf sport", response.getBody().getDescription());
        assertEquals("ACTIVE", response.getBody().getStatus());

        verify(sportService).createSport(requestDTO);
    }

    @Test
    void getAllSportsSuccessfully() {

        SportResponseDTO cricket = new SportResponseDTO();
        cricket.setSportId("SPR002");
        cricket.setName("Cricket");
        cricket.setDescription("Cricket turf sport");
        cricket.setStatus("ACTIVE");

        when(sportService.getAllSports())
                .thenReturn(List.of(responseDTO, cricket));

        ResponseEntity<List<SportResponseDTO>> response =
                sportController.getAllSports();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Football", response.getBody().get(0).getName());
        assertEquals("Cricket", response.getBody().get(1).getName());

        verify(sportService).getAllSports();
    }

    @Test
    void getAllSports_WhenNoSportsExist_ReturnsEmptyList() {

        when(sportService.getAllSports()).thenReturn(List.of());

        ResponseEntity<List<SportResponseDTO>> response =
                sportController.getAllSports();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(sportService).getAllSports();
    }

    @Test
    void getSportByIdSuccessfully() {

        when(sportService.getSportById("SPR001"))
                .thenReturn(responseDTO);

        ResponseEntity<SportResponseDTO> response =
                sportController.getSportById("SPR001");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("SPR001", response.getBody().getSportId());
        assertEquals("Football", response.getBody().getName());

        verify(sportService).getSportById("SPR001");
    }

    @Test
    void getSportByNameSuccessfully() {

        when(sportService.getSportByName("Football"))
                .thenReturn(responseDTO);

        ResponseEntity<SportResponseDTO> response =
                sportController.getSportByName("Football");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("SPR001", response.getBody().getSportId());
        assertEquals("Football", response.getBody().getName());

        verify(sportService).getSportByName("Football");
    }

    @Test
    void updateSportSuccessfully() {

        SportRequestDTO updateRequest = new SportRequestDTO();
        updateRequest.setName("Football 5s");
        updateRequest.setDescription("Updated football description");

        SportResponseDTO updatedResponse = new SportResponseDTO();
        updatedResponse.setSportId("SPR001");
        updatedResponse.setName("Football 5s");
        updatedResponse.setDescription("Updated football description");
        updatedResponse.setStatus("ACTIVE");

        when(sportService.updateSport("SPR001", updateRequest))
                .thenReturn(updatedResponse);

        ResponseEntity<SportResponseDTO> response =
                sportController.updateSport("SPR001", updateRequest);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("SPR001", response.getBody().getSportId());
        assertEquals("Football 5s", response.getBody().getName());
        assertEquals(
                "Updated football description",
                response.getBody().getDescription()
        );

        verify(sportService).updateSport("SPR001", updateRequest);
    }

    @Test
    void deactivateSportSuccessfully() {

        doNothing().when(sportService).deactivateSport("SPR001");

        ResponseEntity<Void> response =
                sportController.deactivateSport("SPR001");

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(sportService).deactivateSport("SPR001");
    }
}