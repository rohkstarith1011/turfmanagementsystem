package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.FacilityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IFacilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityControllerTest {

    @Mock
    private IFacilityService facilityService;

    @InjectMocks
    private FacilityController facilityController;

    private FacilityRequestDTO requestDTO;
    private FacilityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

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
        requestDTO.setRules("No smoking");

        responseDTO = new FacilityResponseDTO();

        responseDTO.setFacilityId("FAC123456");
        responseDTO.setName("Champions Turf");
        responseDTO.setOwnerId("TFO123456");
        responseDTO.setManagerId("TFM123456");
        responseDTO.setLocation("Yelahanka");
        responseDTO.setAddress("Main Road");
        responseDTO.setLocality("New Town");
        responseDTO.setCity("Bangalore");
        responseDTO.setState("Karnataka");
      
        responseDTO.setCapacity(20);
        responseDTO.setOpeningTime(LocalTime.of(6, 0));
        responseDTO.setClosingTime(LocalTime.of(22, 0));
        responseDTO.setBasePrice(1500.0);
        responseDTO.setRules("No smoking");
        responseDTO.setRating(4.5);
        responseDTO.setAvailability(true);
        responseDTO.setStatus("ACTIVE");
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void createFacility_shouldReturnCreatedFacility() {

        when(facilityService.createFacility(requestDTO))
                .thenReturn(responseDTO);

        ResponseEntity<FacilityResponseDTO> response =
                facilityController.createFacility(requestDTO);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("FAC123456",
                response.getBody().getFacilityId());
        assertEquals("Champions Turf",
                response.getBody().getName());

        verify(facilityService).createFacility(requestDTO);
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAllFacilities_shouldReturnAllFacilities() {

        when(facilityService.getAllFacilities())
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getAllFacilities();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Champions Turf",
                response.getBody().get(0).getName());

        verify(facilityService).getAllFacilities();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getFacilityById_shouldReturnFacility() {

        when(facilityService.getFacilityById("FAC123456"))
                .thenReturn(responseDTO);

        ResponseEntity<FacilityResponseDTO> response =
                facilityController.getFacilityById("FAC123456");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("FAC123456",
                response.getBody().getFacilityId());

        verify(facilityService)
                .getFacilityById("FAC123456");
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByName_shouldReturnFacilities() {

        when(facilityService.getFacilitiesByName("Champions Turf"))
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getFacilitiesByName("Champions Turf");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(facilityService)
                .getFacilitiesByName("Champions Turf");
    }

    // ---------------------------------------------------------
    // GET BY CITY
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByCity_shouldReturnFacilities() {

        when(facilityService.getFacilitiesByCity("Bangalore"))
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getFacilitiesByCity("Bangalore");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(facilityService)
                .getFacilitiesByCity("Bangalore");
    }

    // ---------------------------------------------------------
    // GET BY STATE
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByState_shouldReturnFacilities() {

        when(facilityService.getFacilitiesByState("Karnataka"))
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getFacilitiesByState("Karnataka");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(facilityService)
                .getFacilitiesByState("Karnataka");
    }

    // ---------------------------------------------------------
    // GET BY LOCALITY
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByLocality_shouldReturnFacilities() {

        when(facilityService.getFacilitiesByLocality("New Town"))
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getFacilitiesByLocality("New Town");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(facilityService)
                .getFacilitiesByLocality("New Town");
    }

    // ---------------------------------------------------------
    // GET BY TURF TYPE
    // ---------------------------------------------------------

 

    // ---------------------------------------------------------
    // GET BY OWNER
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByOwnerId_shouldReturnFacilities() {

        when(facilityService.getFacilitiesByOwnerId("TFO123456"))
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getFacilitiesByOwnerId("TFO123456");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(facilityService)
                .getFacilitiesByOwnerId("TFO123456");
    }

    // ---------------------------------------------------------
    // GET BY MANAGER
    // ---------------------------------------------------------

    @Test
    void getFacilitiesByManagerId_shouldReturnFacilities() {

        when(facilityService.getFacilitiesByManagerId("TFM123456"))
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<FacilityResponseDTO>> response =
                facilityController.getFacilitiesByManagerId("TFM123456");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(facilityService)
                .getFacilitiesByManagerId("TFM123456");
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateFacility_shouldReturnUpdatedFacility() {

        when(facilityService.updateFacility(
                "FAC123456",
                requestDTO
        )).thenReturn(responseDTO);

        ResponseEntity<FacilityResponseDTO> response =
                facilityController.updateFacility(
                        "FAC123456",
                        requestDTO
                );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("FAC123456",
                response.getBody().getFacilityId());

        verify(facilityService)
                .updateFacility("FAC123456", requestDTO);
    }

    // ---------------------------------------------------------
    // DEACTIVATE
    // ---------------------------------------------------------

    @Test
    void deactivateFacility_shouldReturnNoContent() {

        doNothing()
                .when(facilityService)
                .deactivateFacility("FAC123456");

        ResponseEntity<Void> response =
                facilityController.deactivateFacility("FAC123456");

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(facilityService)
                .deactivateFacility("FAC123456");
    }
}