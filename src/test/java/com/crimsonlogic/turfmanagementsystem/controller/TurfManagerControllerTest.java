package com.crimsonlogic.turfmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfManagerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfManagerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TurfManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ITurfManagerService turfManagerService;

    @InjectMocks
    private TurfManagerController turfManagerController;

    private ObjectMapper objectMapper;

    private TurfManagerRequestDTO requestDTO;
    private TurfManagerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(turfManagerController)
                .build();

        objectMapper = new ObjectMapper();

        requestDTO = new TurfManagerRequestDTO();
        requestDTO.setUserId("USR123456");
        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPhone("9876543210");

        responseDTO = new TurfManagerResponseDTO();
        responseDTO.setTurfManagerId("TFM123456");
        responseDTO.setUserId("USR123456");
        responseDTO.setName("Rohith");
        responseDTO.setEmail("rohith@gmail.com");
        responseDTO.setPhone("9876543210");
        responseDTO.setStatus(UserStatus.ACTIVE);
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void createTurfManager_success() throws Exception {

        when(turfManagerService.createTurfManager(any(TurfManagerRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/turf-managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.turfManagerId").value("TFM123456"))
                .andExpect(jsonPath("$.userId").value("USR123456"))
                .andExpect(jsonPath("$.name").value("Rohith"))
                .andExpect(jsonPath("$.email").value("rohith@gmail.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(turfManagerService)
                .createTurfManager(any(TurfManagerRequestDTO.class));
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAllTurfManagers_success() throws Exception {

        when(turfManagerService.getAllTurfManagers())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/turf-managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].turfManagerId")
                        .value("TFM123456"))
                .andExpect(jsonPath("$[0].userId")
                        .value("USR123456"))
                .andExpect(jsonPath("$[0].name")
                        .value("Rohith"));

        verify(turfManagerService).getAllTurfManagers();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getTurfManagerById_success() throws Exception {

        when(turfManagerService.getTurfManagerById("TFM123456"))
                .thenReturn(responseDTO);

        mockMvc.perform(
                get("/api/turf-managers/TFM123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turfManagerId")
                        .value("TFM123456"))
                .andExpect(jsonPath("$.userId")
                        .value("USR123456"));

        verify(turfManagerService)
                .getTurfManagerById("TFM123456");
    }

    // ---------------------------------------------------------
    // GET BY USER ID
    // ---------------------------------------------------------

    @Test
    void getTurfManagerByUserId_success() throws Exception {

        when(turfManagerService.getTurfManagerByUserId("USR123456"))
                .thenReturn(responseDTO);

        mockMvc.perform(
                get("/api/turf-managers/user/USR123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turfManagerId")
                        .value("TFM123456"))
                .andExpect(jsonPath("$.userId")
                        .value("USR123456"));

        verify(turfManagerService)
                .getTurfManagerByUserId("USR123456");
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------

    @Test
    void getTurfManagersByName_success() throws Exception {

        when(turfManagerService.getTurfManagersByName("Rohith"))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                get("/api/turf-managers/name/Rohith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].turfManagerId")
                        .value("TFM123456"))
                .andExpect(jsonPath("$[0].name")
                        .value("Rohith"));

        verify(turfManagerService)
                .getTurfManagersByName("Rohith");
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateTurfManager_success() throws Exception {

        when(turfManagerService.updateTurfManager(
                eq("TFM123456"),
                any(TurfManagerRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(
                put("/api/turf-managers/TFM123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turfManagerId")
                        .value("TFM123456"))
                .andExpect(jsonPath("$.userId")
                        .value("USR123456"))
                .andExpect(jsonPath("$.name")
                        .value("Rohith"));

        verify(turfManagerService)
                .updateTurfManager(
                        eq("TFM123456"),
                        any(TurfManagerRequestDTO.class));
    }

    // ---------------------------------------------------------
    // DEACTIVATE
    // ---------------------------------------------------------

    @Test
    void deactivateTurfManager_success() throws Exception {

        doNothing()
                .when(turfManagerService)
                .deactivateTurfManager("TFM123456");

        mockMvc.perform(
                patch("/api/turf-managers/TFM123456/deactivate"))
                .andExpect(status().isNoContent());

        verify(turfManagerService)
                .deactivateTurfManager("TFM123456");
    }
}