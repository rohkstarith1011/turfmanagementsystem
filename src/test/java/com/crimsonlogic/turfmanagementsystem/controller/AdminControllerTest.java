package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AdminRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AdminResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private IAdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private AdminRequestDTO requestDTO;
    private AdminResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new AdminRequestDTO();
        requestDTO.setUserId("USR123456");
        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPhone("9876543210");

        responseDTO = new AdminResponseDTO();
        responseDTO.setAdminId("ADM123456");
        responseDTO.setUserId("USR123456");
        responseDTO.setName("Rohith");
        responseDTO.setEmail("rohith@gmail.com");
        responseDTO.setPhone("9876543210");
    }

    @Test
    void createAdmin_success() {

        when(adminService.createAdmin(requestDTO))
                .thenReturn(responseDTO);

        ResponseEntity<AdminResponseDTO> response =
                adminController.createAdmin(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADM123456", response.getBody().getAdminId());
        assertEquals("USR123456", response.getBody().getUserId());

        verify(adminService).createAdmin(requestDTO);
    }

    @Test
    void getAllAdmins_success() {

        when(adminService.getAllAdmins())
                .thenReturn(List.of(responseDTO));

        ResponseEntity<List<AdminResponseDTO>> response =
                adminController.getAllAdmins();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("ADM123456",
                response.getBody().get(0).getAdminId());

        verify(adminService).getAllAdmins();
    }

    @Test
    void getAdminById_success() {

        when(adminService.getAdminById("ADM123456"))
                .thenReturn(responseDTO);

        ResponseEntity<AdminResponseDTO> response =
                adminController.getAdminById("ADM123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADM123456", response.getBody().getAdminId());

        verify(adminService).getAdminById("ADM123456");
    }

    @Test
    void getAdminByUserId_success() {

        when(adminService.getAdminByUserId("USR123456"))
                .thenReturn(responseDTO);

        ResponseEntity<AdminResponseDTO> response =
                adminController.getAdminByUserId("USR123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("USR123456", response.getBody().getUserId());

        verify(adminService).getAdminByUserId("USR123456");
    }

    @Test
    void updateAdmin_success() {

        when(adminService.updateAdmin("ADM123456", requestDTO))
                .thenReturn(responseDTO);

        ResponseEntity<AdminResponseDTO> response =
                adminController.updateAdmin(
                        "ADM123456",
                        requestDTO
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADM123456", response.getBody().getAdminId());

        verify(adminService)
                .updateAdmin("ADM123456", requestDTO);
    }

    @Test
    void deactivateAdmin_success() {

        doNothing()
                .when(adminService)
                .deactivateAdmin("ADM123456");

        ResponseEntity<Void> response =
                adminController.deactivateAdmin("ADM123456");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(adminService).deactivateAdmin("ADM123456");
    }

    @Test
    void createAdmin_serviceThrowsException() {

        when(adminService.createAdmin(requestDTO))
                .thenThrow(new IllegalArgumentException("User not found"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminController.createAdmin(requestDTO)
        );

        assertEquals("User not found", exception.getMessage());

        verify(adminService).createAdmin(requestDTO);
    }

    @Test
    void getAdminById_serviceThrowsException() {

        when(adminService.getAdminById("ADM123456"))
                .thenThrow(new IllegalArgumentException("Admin not found"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminController.getAdminById("ADM123456")
        );

        assertEquals("Admin not found", exception.getMessage());

        verify(adminService).getAdminById("ADM123456");
    }

    @Test
    void updateAdmin_serviceThrowsException() {

        when(adminService.updateAdmin("ADM123456", requestDTO))
                .thenThrow(new IllegalArgumentException("Admin not found"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminController.updateAdmin(
                        "ADM123456",
                        requestDTO
                )
        );

        assertEquals("Admin not found", exception.getMessage());

        verify(adminService)
                .updateAdmin("ADM123456", requestDTO);
    }
}