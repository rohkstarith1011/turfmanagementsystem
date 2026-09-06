package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfOwnerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfOwnerService;
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
class TurfOwnerControllerTest {

    @Mock
    private ITurfOwnerService turfOwnerService;

    @InjectMocks
    private TurfOwnerController turfOwnerController;

    private TurfOwnerRequestDTO requestDTO;
    private TurfOwnerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new TurfOwnerRequestDTO();
        requestDTO.setUserId("USR123456");
        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPhone("9876543210");

        responseDTO = new TurfOwnerResponseDTO();
        responseDTO.setTurfOwnerId("TFO123456");
        responseDTO.setUserId("USR123456");
        responseDTO.setName("Rohith");
        responseDTO.setEmail("rohith@gmail.com");
        responseDTO.setPhone("9876543210");
        responseDTO.setStatus(UserStatus.ACTIVE);
    }

    @Test
    void createTurfOwner_shouldReturnCreatedResponse() {

        when(turfOwnerService.createTurfOwner(requestDTO))
                .thenReturn(responseDTO);

        ResponseEntity<TurfOwnerResponseDTO> response =
                turfOwnerController.createTurfOwner(requestDTO);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("TFO123456",
                response.getBody().getTurfOwnerId());
        assertEquals("USR123456",
                response.getBody().getUserId());

        verify(turfOwnerService).createTurfOwner(requestDTO);
    }

    @Test
    void getAllTurfOwners_shouldReturnAllTurfOwners() {

        when(turfOwnerService.getAllTurfOwners())
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<TurfOwnerResponseDTO>> response =
                turfOwnerController.getAllTurfOwners();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("TFO123456",
                response.getBody().get(0).getTurfOwnerId());

        verify(turfOwnerService).getAllTurfOwners();
    }

    @Test
    void getAllTurfOwners_shouldReturnEmptyList() {

        when(turfOwnerService.getAllTurfOwners())
                .thenReturn(List.of());

        ResponseEntity<List<TurfOwnerResponseDTO>> response =
                turfOwnerController.getAllTurfOwners();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(turfOwnerService).getAllTurfOwners();
    }

    @Test
    void getTurfOwnerById_shouldReturnTurfOwner() {

        when(turfOwnerService.getTurfOwnerById("TFO123456"))
                .thenReturn(responseDTO);

        ResponseEntity<TurfOwnerResponseDTO> response =
                turfOwnerController.getTurfOwnerById("TFO123456");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("TFO123456",
                response.getBody().getTurfOwnerId());

        verify(turfOwnerService)
                .getTurfOwnerById("TFO123456");
    }

    @Test
    void getTurfOwnerByUserId_shouldReturnTurfOwner() {

        when(turfOwnerService.getTurfOwnerByUserId("USR123456"))
                .thenReturn(responseDTO);

        ResponseEntity<TurfOwnerResponseDTO> response =
                turfOwnerController.getTurfOwnerByUserId("USR123456");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("USR123456",
                response.getBody().getUserId());

        verify(turfOwnerService)
                .getTurfOwnerByUserId("USR123456");
    }

    @Test
    void updateTurfOwner_shouldReturnUpdatedTurfOwner() {

        TurfOwnerResponseDTO updatedResponse =
                new TurfOwnerResponseDTO();

        updatedResponse.setTurfOwnerId("TFO123456");
        updatedResponse.setUserId("USR123456");
        updatedResponse.setName("Rohith Updated");
        updatedResponse.setEmail("updated@gmail.com");
        updatedResponse.setPhone("9999999999");
        updatedResponse.setStatus(UserStatus.ACTIVE);

        when(turfOwnerService.updateTurfOwner(
                "TFO123456",
                requestDTO
        )).thenReturn(updatedResponse);

        ResponseEntity<TurfOwnerResponseDTO> response =
                turfOwnerController.updateTurfOwner(
                        "TFO123456",
                        requestDTO
                );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Rohith Updated",
                response.getBody().getName());
        assertEquals("updated@gmail.com",
                response.getBody().getEmail());

        verify(turfOwnerService)
                .updateTurfOwner("TFO123456", requestDTO);
    }

    @Test
    void deactivateTurfOwner_shouldReturnNoContent() {

        doNothing()
                .when(turfOwnerService)
                .deactivateTurfOwner("TFO123456");

        ResponseEntity<Void> response =
                turfOwnerController.deactivateTurfOwner("TFO123456");

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(turfOwnerService)
                .deactivateTurfOwner("TFO123456");
    }
}