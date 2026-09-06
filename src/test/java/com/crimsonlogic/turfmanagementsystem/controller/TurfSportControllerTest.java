package com.crimsonlogic.turfmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfSportRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfSportResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfSportService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TurfSportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ITurfSportService turfSportService;

    @InjectMocks
    private TurfSportController turfSportController;

    private ObjectMapper objectMapper;

    private TurfSportRequestDTO requestDTO;
    private TurfSportResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(turfSportController)
                .build();

        objectMapper = new ObjectMapper();

        requestDTO = new TurfSportRequestDTO();
        requestDTO.setFacilityId("FAC123456");
        requestDTO.setSportId("SPR123456");

        responseDTO = new TurfSportResponseDTO();
        responseDTO.setTurfSportId("TFSPR123456");
        responseDTO.setFacilityId("FAC123456");
        responseDTO.setSportId("SPR123456");
        responseDTO.setStatus("ACTIVE");
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void createTurfSport_success() throws Exception {

        when(turfSportService.createTurfSport(
                any(TurfSportRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(
                post("/api/turf-sports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.turfSportId")
                        .value("TFSPR123456"))
                .andExpect(jsonPath("$.facilityId")
                        .value("FAC123456"))
                .andExpect(jsonPath("$.sportId")
                        .value("SPR123456"))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"));

        verify(turfSportService)
                .createTurfSport(any(TurfSportRequestDTO.class));
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAllTurfSports_success() throws Exception {

        when(turfSportService.getAllTurfSports())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                get("/api/turf-sports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].turfSportId")
                        .value("TFSPR123456"))
                .andExpect(jsonPath("$[0].facilityId")
                        .value("FAC123456"))
                .andExpect(jsonPath("$[0].sportId")
                        .value("SPR123456"));

        verify(turfSportService).getAllTurfSports();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getTurfSportById_success() throws Exception {

        when(turfSportService.getTurfSportById("TFSPR123456"))
                .thenReturn(responseDTO);

        mockMvc.perform(
                get("/api/turf-sports/TFSPR123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turfSportId")
                        .value("TFSPR123456"))
                .andExpect(jsonPath("$.facilityId")
                        .value("FAC123456"))
                .andExpect(jsonPath("$.sportId")
                        .value("SPR123456"));

        verify(turfSportService)
                .getTurfSportById("TFSPR123456");
    }

    // ---------------------------------------------------------
    // GET BY FACILITY ID
    // ---------------------------------------------------------

    @Test
    void getTurfSportsByFacilityId_success() throws Exception {

        when(turfSportService
                .getTurfSportsByFacilityId("FAC123456"))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                get("/api/turf-sports/facility/FAC123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].turfSportId")
                        .value("TFSPR123456"))
                .andExpect(jsonPath("$[0].facilityId")
                        .value("FAC123456"));

        verify(turfSportService)
                .getTurfSportsByFacilityId("FAC123456");
    }

    // ---------------------------------------------------------
    // GET BY SPORT ID
    // ---------------------------------------------------------

    @Test
    void getTurfSportsBySportId_success() throws Exception {

        when(turfSportService
                .getTurfSportsBySportId("SPR123456"))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                get("/api/turf-sports/sport/SPR123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].turfSportId")
                        .value("TFSPR123456"))
                .andExpect(jsonPath("$[0].sportId")
                        .value("SPR123456"));

        verify(turfSportService)
                .getTurfSportsBySportId("SPR123456");
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateTurfSport_success() throws Exception {

        when(turfSportService.updateTurfSport(
                eq("TFSPR123456"),
                any(TurfSportRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(
                put("/api/turf-sports/TFSPR123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turfSportId")
                        .value("TFSPR123456"))
                .andExpect(jsonPath("$.facilityId")
                        .value("FAC123456"))
                .andExpect(jsonPath("$.sportId")
                        .value("SPR123456"));

        verify(turfSportService)
                .updateTurfSport(
                        eq("TFSPR123456"),
                        any(TurfSportRequestDTO.class));
    }

    // ---------------------------------------------------------
    // DEACTIVATE
    // ---------------------------------------------------------

    @Test
    void deactivateTurfSport_success() throws Exception {

        doNothing()
                .when(turfSportService)
                .deactivateTurfSport("TFSPR123456");

        mockMvc.perform(
                patch("/api/turf-sports/TFSPR123456/deactivate"))
                .andExpect(status().isNoContent());

        verify(turfSportService)
                .deactivateTurfSport("TFSPR123456");
    }
}