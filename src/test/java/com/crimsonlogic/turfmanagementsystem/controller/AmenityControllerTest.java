package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AmenityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAmenityService;
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
class AmenityControllerTest {

    @Mock
    private IAmenityService amenityService;

    @InjectMocks
    private AmenityController amenityController;

    private AmenityRequestDTO requestDTO;
    private AmenityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new AmenityRequestDTO();
        requestDTO.setName("Parking");
        requestDTO.setDescription("Parking facility available");

        responseDTO = new AmenityResponseDTO();
        responseDTO.setAmenityId("AMN001");
        responseDTO.setName("Parking");
        responseDTO.setDescription("Parking facility available");
        responseDTO.setStatus("ACTIVE");
    }

    @Test
    void createAmenitySuccessfully() {

        when(amenityService.createAmenity(requestDTO))
                .thenReturn(responseDTO);

        ResponseEntity<AmenityResponseDTO> response =
                amenityController.createAmenity(requestDTO);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("AMN001", response.getBody().getAmenityId());
        assertEquals("Parking", response.getBody().getName());
        assertEquals(
                "Parking facility available",
                response.getBody().getDescription()
        );
        assertEquals("ACTIVE", response.getBody().getStatus());

        verify(amenityService).createAmenity(requestDTO);
    }

    @Test
    void getAllAmenitiesSuccessfully() {

        AmenityResponseDTO washroom = new AmenityResponseDTO();
        washroom.setAmenityId("AMN002");
        washroom.setName("Washroom");
        washroom.setDescription("Washroom facility available");
        washroom.setStatus("ACTIVE");

        when(amenityService.getAllAmenities())
                .thenReturn(List.of(responseDTO, washroom));

        ResponseEntity<List<AmenityResponseDTO>> response =
                amenityController.getAllAmenities();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Parking", response.getBody().get(0).getName());
        assertEquals("Washroom", response.getBody().get(1).getName());

        verify(amenityService).getAllAmenities();
    }

    @Test
    void getAllAmenities_WhenNoAmenitiesExist_ReturnsEmptyList() {

        when(amenityService.getAllAmenities())
                .thenReturn(List.of());

        ResponseEntity<List<AmenityResponseDTO>> response =
                amenityController.getAllAmenities();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(amenityService).getAllAmenities();
    }

    @Test
    void getAmenityByIdSuccessfully() {

        when(amenityService.getAmenityById("AMN001"))
                .thenReturn(responseDTO);

        ResponseEntity<AmenityResponseDTO> response =
                amenityController.getAmenityById("AMN001");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("AMN001", response.getBody().getAmenityId());
        assertEquals("Parking", response.getBody().getName());

        verify(amenityService).getAmenityById("AMN001");
    }

    @Test
    void getAmenityByNameSuccessfully() {

        when(amenityService.getAmenityByName("Parking"))
                .thenReturn(responseDTO);

        ResponseEntity<AmenityResponseDTO> response =
                amenityController.getAmenityByName("Parking");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("AMN001", response.getBody().getAmenityId());
        assertEquals("Parking", response.getBody().getName());

        verify(amenityService).getAmenityByName("Parking");
    }

    @Test
    void updateAmenitySuccessfully() {

        AmenityRequestDTO updateRequest = new AmenityRequestDTO();
        updateRequest.setName("Premium Parking");
        updateRequest.setDescription(
                "Premium parking facility available"
        );

        AmenityResponseDTO updatedResponse =
                new AmenityResponseDTO();
        updatedResponse.setAmenityId("AMN001");
        updatedResponse.setName("Premium Parking");
        updatedResponse.setDescription(
                "Premium parking facility available"
        );
        updatedResponse.setStatus("ACTIVE");

        when(amenityService.updateAmenity(
                "AMN001",
                updateRequest
        )).thenReturn(updatedResponse);

        ResponseEntity<AmenityResponseDTO> response =
                amenityController.updateAmenity(
                        "AMN001",
                        updateRequest
                );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(
                "AMN001",
                response.getBody().getAmenityId()
        );
        assertEquals(
                "Premium Parking",
                response.getBody().getName()
        );
        assertEquals(
                "Premium parking facility available",
                response.getBody().getDescription()
        );

        verify(amenityService).updateAmenity(
                "AMN001",
                updateRequest
        );
    }

    @Test
    void deactivateAmenitySuccessfully() {

        doNothing()
                .when(amenityService)
                .deactivateAmenity("AMN001");

        ResponseEntity<Void> response =
                amenityController.deactivateAmenity("AMN001");

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(amenityService)
                .deactivateAmenity("AMN001");
    }
}