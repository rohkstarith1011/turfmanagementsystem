package com.crimsonlogic.turfmanagementsystem.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserService;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUserService userService;

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPassword("Password123");
        requestDTO.setPhone("9876543210");

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId("USR123456");
        responseDTO.setName("Rohith");
        responseDTO.setEmail("rohith@gmail.com");
        responseDTO.setPhone("9876543210");
        responseDTO.setStatus(UserStatus.ACTIVE);

        when(userService.createUser(any(UserRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("USR123456"))
                .andExpect(jsonPath("$.name").value("Rohith"))
                .andExpect(jsonPath("$.email").value("rohith@gmail.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(userService).createUser(any(UserRequestDTO.class));
    }

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {

        UserResponseDTO user1 = new UserResponseDTO();
        user1.setUserId("USR123456");
        user1.setName("Rohith");
        user1.setEmail("rohith@gmail.com");
        user1.setPhone("9876543210");
        user1.setStatus(UserStatus.ACTIVE);

        UserResponseDTO user2 = new UserResponseDTO();
        user2.setUserId("USR654321");
        user2.setName("Rahul");
        user2.setEmail("rahul@gmail.com");
        user2.setPhone("9876543211");
        user2.setStatus(UserStatus.ACTIVE);

        List<UserResponseDTO> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value("USR123456"))
                .andExpect(jsonPath("$[1].userId").value("USR654321"));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId("USR123456");
        responseDTO.setName("Rohith");
        responseDTO.setEmail("rohith@gmail.com");
        responseDTO.setPhone("9876543210");
        responseDTO.setStatus(UserStatus.ACTIVE);

        when(userService.getUserById("USR123456"))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/users/USR123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("USR123456"))
                .andExpect(jsonPath("$.name").value("Rohith"))
                .andExpect(jsonPath("$.email").value("rohith@gmail.com"));

        verify(userService).getUserById("USR123456");
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Rohith Updated");
        requestDTO.setEmail("rohith.updated@gmail.com");
        requestDTO.setPassword("NewPassword123");
        requestDTO.setPhone("9876543210");

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId("USR123456");
        responseDTO.setName("Rohith Updated");
        responseDTO.setEmail("rohith.updated@gmail.com");
        responseDTO.setPhone("9876543210");
        responseDTO.setStatus(UserStatus.ACTIVE);

        when(userService.updateUser(
                eq("USR123456"),
                any(UserRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/users/USR123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("USR123456"))
                .andExpect(jsonPath("$.name").value("Rohith Updated"))
                .andExpect(jsonPath("$.email")
                        .value("rohith.updated@gmail.com"));

        verify(userService).updateUser(
                eq("USR123456"),
                any(UserRequestDTO.class));
    }

    @Test
    void deactivateUser_shouldReturnNoContent() throws Exception {

        doNothing().when(userService)
                .deactivateUser("USR123456");

        mockMvc.perform(patch("/api/users/USR123456/deactivate"))
                .andExpect(status().isNoContent());

        verify(userService).deactivateUser("USR123456");
    }

    @Test
    void createUser_withInvalidData_shouldReturnBadRequest() throws Exception {

        UserRequestDTO requestDTO = new UserRequestDTO();

        requestDTO.setName("");
        requestDTO.setEmail("invalid-email");
        requestDTO.setPassword("123");
        requestDTO.setPhone("123");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_withInvalidData_shouldReturnBadRequest() throws Exception {

        UserRequestDTO requestDTO = new UserRequestDTO();

        requestDTO.setName("");
        requestDTO.setEmail("invalid-email");
        requestDTO.setPassword("123");
        requestDTO.setPhone("123");

        mockMvc.perform(put("/api/users/USR123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}