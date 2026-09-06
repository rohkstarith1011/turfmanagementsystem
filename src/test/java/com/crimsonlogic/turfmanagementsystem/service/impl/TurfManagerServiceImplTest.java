package com.crimsonlogic.turfmanagementsystem.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfManagerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfManagerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.TurfManager;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.TurfManagerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;

@ExtendWith(MockitoExtension.class)
class TurfManagerServiceImplTest {

    @Mock
    private TurfManagerRepository turfManagerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private TurfManagerServiceImpl turfManagerService;

    private User user;
    private TurfManager turfManager;
    private TurfManagerRequestDTO requestDTO;
    private Role managerRole;
    private UserRole userRole;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId("USR123456");
        user.setName("Rohith");
        user.setEmail("rohith@gmail.com");
        user.setPhone("9876543210");
        user.setStatus(UserStatus.ACTIVE);

        managerRole = new Role();
        managerRole.setRoleId("ROL123456");
        managerRole.setRoleName("MANAGER");

        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(managerRole);

        requestDTO = new TurfManagerRequestDTO();
        requestDTO.setUserId("USR123456");
        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPhone("9876543210");

        turfManager = new TurfManager();
        turfManager.setTurfManagerId("TFM123456");
        turfManager.setUser(user);
        turfManager.setName("Rohith");
        turfManager.setEmail("rohith@gmail.com");
        turfManager.setPhone("9876543210");
        turfManager.setStatus(UserStatus.ACTIVE);
    }

    // ---------------------------------------------------------
    // CREATE TESTS
    // ---------------------------------------------------------

    @Test
    void createTurfManager_success() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(turfManagerRepository.existsByUserUserId("USR123456"))
                .thenReturn(false);

        when(turfManagerRepository.save(any(TurfManager.class)))
                .thenReturn(turfManager);

        TurfManagerResponseDTO response =
                turfManagerService.createTurfManager(requestDTO);

        assertNotNull(response);
        assertEquals("TFM123456", response.getTurfManagerId());
        assertEquals("USR123456", response.getUserId());
        assertEquals("Rohith", response.getName());
        assertEquals("rohith@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getPhone());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        verify(userRepository).findById("USR123456");
        verify(userRoleRepository).findByUserUserId("USR123456");
        verify(turfManagerRepository).existsByUserUserId("USR123456");
        verify(turfManagerRepository).save(any(TurfManager.class));
    }

    @Test
    void createTurfManager_userNotFound() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.createTurfManager(requestDTO));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById("USR123456");
        verifyNoInteractions(userRoleRepository);
        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void createTurfManager_inactiveUser() {

        user.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.createTurfManager(requestDTO));

        assertEquals(
                "Cannot create Turf Manager profile for an inactive User",
                exception.getMessage());

        verify(userRepository).findById("USR123456");
        verifyNoInteractions(userRoleRepository);
        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void createTurfManager_userDoesNotHaveManagerRole() {

        Role playerRole = new Role();
        playerRole.setRoleId("ROL654321");
        playerRole.setRoleName("PLAYER");

        UserRole playerUserRole = new UserRole();
        playerUserRole.setUser(user);
        playerUserRole.setRole(playerRole);

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(playerUserRole));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.createTurfManager(requestDTO));

        assertEquals(
                "User does not have the MANAGER role",
                exception.getMessage());

        verify(userRoleRepository).findByUserUserId("USR123456");
        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void createTurfManager_duplicateProfile() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(turfManagerRepository.existsByUserUserId("USR123456"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.createTurfManager(requestDTO));

        assertEquals(
                "Turf Manager profile already exists for this User",
                exception.getMessage());

        verify(turfManagerRepository)
                .existsByUserUserId("USR123456");

        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void createTurfManager_detailsDoNotMatchUser() {

        requestDTO.setName("Different Name");

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(turfManagerRepository.existsByUserUserId("USR123456"))
                .thenReturn(false);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.createTurfManager(requestDTO));

        assertEquals(
                "Turf Manager details must match the linked User",
                exception.getMessage());

        verify(turfManagerRepository, never()).save(any());
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAllTurfManagers_success() {

        TurfManager secondManager = new TurfManager();
        secondManager.setTurfManagerId("TFM654321");
        secondManager.setUser(user);
        secondManager.setName("Rahul");
        secondManager.setEmail("rahul@gmail.com");
        secondManager.setPhone("9123456789");
        secondManager.setStatus(UserStatus.ACTIVE);

        when(turfManagerRepository.findAll())
                .thenReturn(Arrays.asList(turfManager, secondManager));

        List<TurfManagerResponseDTO> response =
                turfManagerService.getAllTurfManagers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("TFM123456", response.get(0).getTurfManagerId());
        assertEquals("TFM654321", response.get(1).getTurfManagerId());

        verify(turfManagerRepository).findAll();
    }

    @Test
    void getAllTurfManagers_empty() {

        when(turfManagerRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<TurfManagerResponseDTO> response =
                turfManagerService.getAllTurfManagers();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(turfManagerRepository).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getTurfManagerById_success() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        TurfManagerResponseDTO response =
                turfManagerService.getTurfManagerById("TFM123456");

        assertNotNull(response);
        assertEquals("TFM123456", response.getTurfManagerId());
        assertEquals("USR123456", response.getUserId());

        verify(turfManagerRepository).findById("TFM123456");
    }

    @Test
    void getTurfManagerById_notFound() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.getTurfManagerById("TFM123456"));

        assertEquals("Turf Manager not found", exception.getMessage());

        verify(turfManagerRepository).findById("TFM123456");
    }

    // ---------------------------------------------------------
    // GET BY USER ID
    // ---------------------------------------------------------

    @Test
    void getTurfManagerByUserId_success() {

        when(turfManagerRepository.findByUserUserId("USR123456"))
                .thenReturn(Optional.of(turfManager));

        TurfManagerResponseDTO response =
                turfManagerService.getTurfManagerByUserId("USR123456");

        assertNotNull(response);
        assertEquals("TFM123456", response.getTurfManagerId());
        assertEquals("USR123456", response.getUserId());

        verify(turfManagerRepository)
                .findByUserUserId("USR123456");
    }

    @Test
    void getTurfManagerByUserId_notFound() {

        when(turfManagerRepository.findByUserUserId("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.getTurfManagerByUserId("USR123456"));

        assertEquals(
                "Turf Manager profile not found for this User",
                exception.getMessage());

        verify(turfManagerRepository)
                .findByUserUserId("USR123456");
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------

    @Test
    void getTurfManagersByName_success() {

        TurfManager secondManager = new TurfManager();
        secondManager.setTurfManagerId("TFM654321");
        secondManager.setUser(user);
        secondManager.setName("Rahul");
        secondManager.setEmail("rahul@gmail.com");
        secondManager.setPhone("9123456789");
        secondManager.setStatus(UserStatus.ACTIVE);

        when(turfManagerRepository.findAll())
                .thenReturn(Arrays.asList(turfManager, secondManager));

        List<TurfManagerResponseDTO> response =
                turfManagerService.getTurfManagersByName("rohith");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Rohith", response.get(0).getName());

        verify(turfManagerRepository).findAll();
    }

    @Test
    void getTurfManagersByName_noMatch() {

        when(turfManagerRepository.findAll())
                .thenReturn(List.of(turfManager));

        List<TurfManagerResponseDTO> response =
                turfManagerService.getTurfManagersByName("Unknown");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(turfManagerRepository).findAll();
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateTurfManager_success() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        when(turfManagerRepository.save(any(TurfManager.class)))
                .thenReturn(turfManager);

        TurfManagerResponseDTO response =
                turfManagerService.updateTurfManager(
                        "TFM123456",
                        requestDTO);

        assertNotNull(response);
        assertEquals("TFM123456", response.getTurfManagerId());
        assertEquals("USR123456", response.getUserId());

        verify(turfManagerRepository).findById("TFM123456");
        verify(userRepository).findById("USR123456");
        verify(userRoleRepository).findByUserUserId("USR123456");
        verify(userRepository).save(user);
        verify(turfManagerRepository).save(turfManager);
    }

    @Test
    void updateTurfManager_notFound() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.updateTurfManager(
                                "TFM123456",
                                requestDTO));

        assertEquals("Turf Manager not found", exception.getMessage());

        verify(turfManagerRepository).findById("TFM123456");
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateTurfManager_userNotFound() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.updateTurfManager(
                                "TFM123456",
                                requestDTO));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById("USR123456");
        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void updateTurfManager_reassignmentNotAllowed() {

        requestDTO.setUserId("USR999999");

        User anotherUser = new User();
        anotherUser.setUserId("USR999999");
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@gmail.com");
        anotherUser.setPhone("9999999999");
        anotherUser.setStatus(UserStatus.ACTIVE);

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        when(userRepository.findById("USR999999"))
                .thenReturn(Optional.of(anotherUser));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.updateTurfManager(
                                "TFM123456",
                                requestDTO));

        assertEquals(
                "Turf Manager cannot be reassigned to another User",
                exception.getMessage());

        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void updateTurfManager_inactiveUser() {

        user.setStatus(UserStatus.INACTIVE);

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.updateTurfManager(
                                "TFM123456",
                                requestDTO));

        assertEquals(
                "Cannot update Turf Manager profile for an inactive User",
                exception.getMessage());

        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void updateTurfManager_userDoesNotHaveManagerRole() {

        Role playerRole = new Role();
        playerRole.setRoleName("PLAYER");

        UserRole playerUserRole = new UserRole();
        playerUserRole.setUser(user);
        playerUserRole.setRole(playerRole);

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(playerUserRole));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.updateTurfManager(
                                "TFM123456",
                                requestDTO));

        assertEquals(
                "User does not have the MANAGER role",
                exception.getMessage());

        verify(turfManagerRepository, never()).save(any());
    }

    @Test
    void updateTurfManager_detailsDoNotMatchUser() {

        requestDTO.setEmail("different@gmail.com");

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.updateTurfManager(
                                "TFM123456",
                                requestDTO));

        assertEquals(
                "Turf Manager details must match the linked User",
                exception.getMessage());

        verify(turfManagerRepository, never()).save(any());
    }

    // ---------------------------------------------------------
    // DEACTIVATE
    // ---------------------------------------------------------

    @Test
    void deactivateTurfManager_success() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.of(turfManager));

        turfManagerService.deactivateTurfManager("TFM123456");

        assertEquals(UserStatus.INACTIVE, turfManager.getStatus());

        verify(turfManagerRepository).findById("TFM123456");
        verify(turfManagerRepository).save(turfManager);
    }

    @Test
    void deactivateTurfManager_notFound() {

        when(turfManagerRepository.findById("TFM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfManagerService.deactivateTurfManager(
                                "TFM123456"));

        assertEquals("Turf Manager not found", exception.getMessage());

        verify(turfManagerRepository, never()).save(any());
    }
}