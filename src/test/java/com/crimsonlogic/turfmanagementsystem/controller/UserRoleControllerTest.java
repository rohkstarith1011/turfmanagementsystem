package com.crimsonlogic.turfmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRoleRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserRoleResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserRoleService;

@ExtendWith(MockitoExtension.class)
class UserRoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserRoleService userRoleService;

    private UserRoleResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        UserRoleController controller =
                new UserRoleController(userRoleService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        responseDTO = new UserRoleResponseDTO();
        responseDTO.setUserRoleId("USRL123456");
        responseDTO.setUserId("USR123456");
        responseDTO.setRoleId("ROL123456");
        responseDTO.setRoleName("PLAYER");
    }

    @Test
    void assignRole_ShouldReturnCreated() throws Exception {

        when(userRoleService.assignRole(any(UserRoleRequestDTO.class)))
                .thenReturn(responseDTO);

        String requestBody = """
                {
                    "userId": "USR123456",
                    "roleId": "ROL123456"
                }
                """;

        mockMvc.perform(post("/api/user-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userRoleId")
                        .value("USRL123456"))
                .andExpect(jsonPath("$.userId")
                        .value("USR123456"))
                .andExpect(jsonPath("$.roleId")
                        .value("ROL123456"))
                .andExpect(jsonPath("$.roleName")
                        .value("PLAYER"));

        verify(userRoleService)
                .assignRole(any(UserRoleRequestDTO.class));
    }

    @Test
    void assignRole_ShouldReturnBadRequest_WhenUserIdIsBlank()
            throws Exception {

        String requestBody = """
                {
                    "userId": "",
                    "roleId": "ROL123456"
                }
                """;

        mockMvc.perform(post("/api/user-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void assignRole_ShouldReturnBadRequest_WhenRoleIdIsBlank()
            throws Exception {

        String requestBody = """
                {
                    "userId": "USR123456",
                    "roleId": ""
                }
                """;

        mockMvc.perform(post("/api/user-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUserRoles_ShouldReturnOk() throws Exception {

        when(userRoleService.getAllUserRoles())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/user-roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userRoleId")
                        .value("USRL123456"))
                .andExpect(jsonPath("$[0].userId")
                        .value("USR123456"))
                .andExpect(jsonPath("$[0].roleId")
                        .value("ROL123456"))
                .andExpect(jsonPath("$[0].roleName")
                        .value("PLAYER"));

        verify(userRoleService).getAllUserRoles();
    }

    @Test
    void getAllUserRoles_ShouldReturnEmptyList() throws Exception {

        when(userRoleService.getAllUserRoles())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/user-roles"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getUserRoleById_ShouldReturnOk() throws Exception {

        when(userRoleService.getUserRoleById("USRL123456"))
                .thenReturn(responseDTO);

        mockMvc.perform(
                        get("/api/user-roles/USRL123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRoleId")
                        .value("USRL123456"))
                .andExpect(jsonPath("$.roleName")
                        .value("PLAYER"));

        verify(userRoleService)
                .getUserRoleById("USRL123456");
    }

    @Test
    void getRolesByUserId_ShouldReturnOk() throws Exception {

        when(userRoleService.getRolesByUserId("USR123456"))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                        get("/api/user-roles/user/USR123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId")
                        .value("USR123456"))
                .andExpect(jsonPath("$[0].roleId")
                        .value("ROL123456"))
                .andExpect(jsonPath("$[0].roleName")
                        .value("PLAYER"));

        verify(userRoleService)
                .getRolesByUserId("USR123456");
    }

    @Test
    void getRolesByUserId_ShouldReturnEmptyList() throws Exception {

        when(userRoleService.getRolesByUserId("USR123456"))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/user-roles/user/USR123456"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void removeRole_ShouldReturnNoContent() throws Exception {

        doNothing()
                .when(userRoleService)
                .removeRole("USRL123456");

        mockMvc.perform(
                        delete("/api/user-roles/USRL123456"))
                .andExpect(status().isNoContent());

        verify(userRoleService)
                .removeRole("USRL123456");
    }
}