package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfOwnerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.TurfOwner;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.TurfOwnerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurfOwnerServiceImplTest {

    @Mock
    private TurfOwnerRepository turfOwnerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private TurfOwnerServiceImpl turfOwnerService;

    private User user;
    private Role ownerRole;
    private UserRole userRole;
    private TurfOwner turfOwner;
    private TurfOwnerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId("USR123456");
        user.setName("Rohith");
        user.setEmail("rohith@gmail.com");
        user.setPhone("9876543210");
        user.setStatus(UserStatus.ACTIVE);

        ownerRole = new Role();
        ownerRole.setRoleId("ROL123456");
        ownerRole.setRoleName("OWNER");

        userRole = new UserRole();
        userRole.setUserRoleId("USRL123456");
        userRole.setUser(user);
        userRole.setRole(ownerRole);

        turfOwner = new TurfOwner();
        turfOwner.setTurfOwnerId("TFO123456");
        turfOwner.setUser(user);
        turfOwner.setName("Rohith");
        turfOwner.setEmail("rohith@gmail.com");
        turfOwner.setPhone("9876543210");
        turfOwner.setStatus(UserStatus.ACTIVE);

        requestDTO = new TurfOwnerRequestDTO();
        requestDTO.setUserId("USR123456");
        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPhone("9876543210");
    }

    @Test
    void createTurfOwner_shouldCreateSuccessfully() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(turfOwnerRepository.existsByUserUserId("USR123456"))
                .thenReturn(false);

        when(turfOwnerRepository.save(any(TurfOwner.class)))
                .thenReturn(turfOwner);

        TurfOwnerResponseDTO response =
                turfOwnerService.createTurfOwner(requestDTO);

        assertNotNull(response);
        assertEquals("TFO123456", response.getTurfOwnerId());
        assertEquals("USR123456", response.getUserId());
        assertEquals("Rohith", response.getName());
        assertEquals("rohith@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getPhone());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        verify(turfOwnerRepository).save(any(TurfOwner.class));
    }

    @Test
    void createTurfOwner_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.createTurfOwner(requestDTO)
                );

        assertEquals("User not found", exception.getMessage());

        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void createTurfOwner_shouldThrowExceptionWhenUserInactive() {

        user.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.createTurfOwner(requestDTO)
                );

        assertEquals(
                "Only active users can become turf owners",
                exception.getMessage()
        );

        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void createTurfOwner_shouldThrowExceptionWhenUserDoesNotHaveOwnerRole() {

        Role playerRole = new Role();
        playerRole.setRoleId("ROL654321");
        playerRole.setRoleName("PLAYER");

        UserRole playerUserRole = new UserRole();
        playerUserRole.setUserRoleId("USRL654321");
        playerUserRole.setUser(user);
        playerUserRole.setRole(playerRole);

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(playerUserRole));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.createTurfOwner(requestDTO)
                );

        assertEquals(
                "User does not have OWNER role",
                exception.getMessage()
        );

        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void createTurfOwner_shouldThrowExceptionWhenProfileAlreadyExists() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(turfOwnerRepository.existsByUserUserId("USR123456"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.createTurfOwner(requestDTO)
                );

        assertEquals(
                "Turf owner profile already exists for this user",
                exception.getMessage()
        );

        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void createTurfOwner_shouldThrowExceptionWhenDetailsDoNotMatchUser() {

        requestDTO.setName("Different Name");

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(turfOwnerRepository.existsByUserUserId("USR123456"))
                .thenReturn(false);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.createTurfOwner(requestDTO)
                );

        assertEquals(
                "Turf owner details must match the linked user details",
                exception.getMessage()
        );

        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void getAllTurfOwners_shouldReturnAllTurfOwners() {

        when(turfOwnerRepository.findAll())
                .thenReturn(List.of(turfOwner));

        List<TurfOwnerResponseDTO> response =
                turfOwnerService.getAllTurfOwners();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("TFO123456", response.get(0).getTurfOwnerId());
        assertEquals("Rohith", response.get(0).getName());

        verify(turfOwnerRepository).findAll();
    }

    @Test
    void getAllTurfOwners_shouldReturnEmptyListWhenNoTurfOwnersExist() {

        when(turfOwnerRepository.findAll())
                .thenReturn(List.of());

        List<TurfOwnerResponseDTO> response =
                turfOwnerService.getAllTurfOwners();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(turfOwnerRepository).findAll();
    }

    @Test
    void getTurfOwnerById_shouldReturnTurfOwner() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        TurfOwnerResponseDTO response =
                turfOwnerService.getTurfOwnerById("TFO123456");

        assertNotNull(response);
        assertEquals("TFO123456", response.getTurfOwnerId());
        assertEquals("USR123456", response.getUserId());
        assertEquals("Rohith", response.getName());
    }

    @Test
    void getTurfOwnerById_shouldThrowExceptionWhenNotFound() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.getTurfOwnerById("TFO123456")
                );

        assertEquals("Turf owner not found", exception.getMessage());
    }

    @Test
    void getTurfOwnerByUserId_shouldReturnTurfOwner() {

        when(turfOwnerRepository.findByUserUserId("USR123456"))
                .thenReturn(Optional.of(turfOwner));

        TurfOwnerResponseDTO response =
                turfOwnerService.getTurfOwnerByUserId("USR123456");

        assertNotNull(response);
        assertEquals("TFO123456", response.getTurfOwnerId());
        assertEquals("USR123456", response.getUserId());
    }

    @Test
    void getTurfOwnerByUserId_shouldThrowExceptionWhenNotFound() {

        when(turfOwnerRepository.findByUserUserId("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.getTurfOwnerByUserId("USR123456")
                );

        assertEquals(
                "Turf owner profile not found for user",
                exception.getMessage()
        );
    }

    @Test
    void updateTurfOwner_shouldUpdateSuccessfully() {

        TurfOwnerRequestDTO updateRequest = new TurfOwnerRequestDTO();
        updateRequest.setUserId("USR123456");
        updateRequest.setName("Rohith Updated");
        updateRequest.setEmail("rohith.updated@gmail.com");
        updateRequest.setPhone("9999999999");

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        when(turfOwnerRepository.save(any(TurfOwner.class)))
                .thenReturn(turfOwner);

        TurfOwnerResponseDTO response =
                turfOwnerService.updateTurfOwner(
                        "TFO123456",
                        updateRequest
                );

        assertNotNull(response);
        assertEquals("Rohith Updated", user.getName());
        assertEquals("rohith.updated@gmail.com", user.getEmail());
        assertEquals("9999999999", user.getPhone());

        assertEquals("Rohith Updated", turfOwner.getName());
        assertEquals("rohith.updated@gmail.com", turfOwner.getEmail());
        assertEquals("9999999999", turfOwner.getPhone());

        verify(userRepository).save(user);
        verify(turfOwnerRepository).save(turfOwner);
    }

    @Test
    void updateTurfOwner_shouldThrowExceptionWhenTurfOwnerNotFound() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.updateTurfOwner(
                                "TFO123456",
                                requestDTO
                        )
                );

        assertEquals("Turf owner not found", exception.getMessage());

        verify(userRepository, never()).save(any());
        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void updateTurfOwner_shouldThrowExceptionWhenUserNotFound() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.updateTurfOwner(
                                "TFO123456",
                                requestDTO
                        )
                );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never()).save(any());
        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void updateTurfOwner_shouldThrowExceptionWhenReassigningUser() {

        User anotherUser = new User();
        anotherUser.setUserId("USR654321");
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@gmail.com");
        anotherUser.setPhone("8888888888");
        anotherUser.setStatus(UserStatus.ACTIVE);

        requestDTO.setUserId("USR654321");

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        when(userRepository.findById("USR654321"))
                .thenReturn(Optional.of(anotherUser));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.updateTurfOwner(
                                "TFO123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Turf owner profile cannot be reassigned to another user",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any());
        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void updateTurfOwner_shouldThrowExceptionWhenUserInactive() {

        user.setStatus(UserStatus.INACTIVE);

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.updateTurfOwner(
                                "TFO123456",
                                requestDTO
                        )
                );

        assertEquals(
                "Only active users can be turf owners",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any());
        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void updateTurfOwner_shouldThrowExceptionWhenUserDoesNotHaveOwnerRole() {

        Role playerRole = new Role();
        playerRole.setRoleId("ROL654321");
        playerRole.setRoleName("PLAYER");

        UserRole playerUserRole = new UserRole();
        playerUserRole.setUserRoleId("USRL654321");
        playerUserRole.setUser(user);
        playerUserRole.setRole(playerRole);

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(playerUserRole));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.updateTurfOwner(
                                "TFO123456",
                                requestDTO
                        )
                );

        assertEquals(
                "User does not have OWNER role",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any());
        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void deactivateTurfOwner_shouldDeactivateSuccessfully() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        turfOwnerService.deactivateTurfOwner("TFO123456");

        assertEquals(UserStatus.INACTIVE, turfOwner.getStatus());

        verify(turfOwnerRepository).save(turfOwner);
    }

    @Test
    void deactivateTurfOwner_shouldThrowExceptionWhenNotFound() {

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.deactivateTurfOwner("TFO123456")
                );

        assertEquals("Turf owner not found", exception.getMessage());

        verify(turfOwnerRepository, never()).save(any());
    }

    @Test
    void deactivateTurfOwner_shouldThrowExceptionWhenAlreadyInactive() {

        turfOwner.setStatus(UserStatus.INACTIVE);

        when(turfOwnerRepository.findById("TFO123456"))
                .thenReturn(Optional.of(turfOwner));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> turfOwnerService.deactivateTurfOwner("TFO123456")
                );

        assertEquals(
                "Turf owner is already inactive",
                exception.getMessage()
        );

        verify(turfOwnerRepository, never()).save(any());
    }
}