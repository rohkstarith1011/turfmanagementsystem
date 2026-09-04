package com.crimsonlogic.turfmanagementsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRoleRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserRoleResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    private User user;
    private Role role;
    private UserRole userRole;
    private UserRoleRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId("USR123456");
        user.setName("Rohith");
        user.setStatus(UserStatus.ACTIVE);
        role = new Role();
        role.setRoleId("ROL123456");
        role.setRoleName("PLAYER");

        userRole = new UserRole();
        userRole.setUserRoleId("USRL123456");
        userRole.setUser(user);
        userRole.setRole(role);

        requestDTO = new UserRoleRequestDTO();
        requestDTO.setUserId("USR123456");
        requestDTO.setRoleId("ROL123456");
    }

    @Test
    void assignRole_ShouldAssignRoleSuccessfully() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(roleRepository.findById("ROL123456"))
                .thenReturn(Optional.of(role));

        when(userRoleRepository.existsByUserUserIdAndRoleRoleId(
                "USR123456", "ROL123456"))
                .thenReturn(false);

        when(userRoleRepository.save(any(UserRole.class)))
                .thenReturn(userRole);

        UserRoleResponseDTO response =
                userRoleService.assignRole(requestDTO);

        assertEquals("USRL123456", response.getUserRoleId());
        assertEquals("USR123456", response.getUserId());
        assertEquals("ROL123456", response.getRoleId());
        assertEquals("PLAYER", response.getRoleName());

        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void assignRole_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.assignRole(requestDTO));

        assertEquals(
                "User not found with ID: USR123456",
                exception.getMessage());

        verify(roleRepository, never()).findById(any());
        verify(userRoleRepository, never()).save(any());
    }

    @Test
    void assignRole_ShouldThrowException_WhenRoleDoesNotExist() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(roleRepository.findById("ROL123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.assignRole(requestDTO));

        assertEquals(
                "Role not found with ID: ROL123456",
                exception.getMessage());

        verify(userRoleRepository, never()).save(any());
    }

    @Test
    void assignRole_ShouldThrowException_WhenRoleAlreadyAssigned() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(roleRepository.findById("ROL123456"))
                .thenReturn(Optional.of(role));

        when(userRoleRepository.existsByUserUserIdAndRoleRoleId(
                "USR123456", "ROL123456"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.assignRole(requestDTO));

        assertEquals(
                "Role is already assigned to this user",
                exception.getMessage());

        verify(userRoleRepository, never()).save(any());
    }

    @Test
    void getAllUserRoles_ShouldReturnAllMappings() {

        when(userRoleRepository.findAll())
                .thenReturn(List.of(userRole));

        List<UserRoleResponseDTO> response =
                userRoleService.getAllUserRoles();

        assertEquals(1, response.size());
        assertEquals("USRL123456", response.get(0).getUserRoleId());
        assertEquals("USR123456", response.get(0).getUserId());
        assertEquals("ROL123456", response.get(0).getRoleId());
        assertEquals("PLAYER", response.get(0).getRoleName());
    }

    @Test
    void getUserRoleById_ShouldReturnMapping() {

        when(userRoleRepository.findById("USRL123456"))
                .thenReturn(Optional.of(userRole));

        UserRoleResponseDTO response =
                userRoleService.getUserRoleById("USRL123456");

        assertEquals("USRL123456", response.getUserRoleId());
        assertEquals("USR123456", response.getUserId());
        assertEquals("ROL123456", response.getRoleId());
        assertEquals("PLAYER", response.getRoleName());
    }

    @Test
    void getUserRoleById_ShouldThrowException_WhenMappingDoesNotExist() {

        when(userRoleRepository.findById("USRL123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.getUserRoleById("USRL123456"));

        assertEquals(
                "UserRole not found with ID: USRL123456",
                exception.getMessage());
    }

    @Test
    void getRolesByUserId_ShouldReturnUserRoles() {

        when(userRepository.existsById("USR123456"))
                .thenReturn(true);

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        List<UserRoleResponseDTO> response =
                userRoleService.getRolesByUserId("USR123456");

        assertEquals(1, response.size());
        assertEquals("USR123456", response.get(0).getUserId());
        assertEquals("PLAYER", response.get(0).getRoleName());
    }

    @Test
    void getRolesByUserId_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.existsById("USR123456"))
                .thenReturn(false);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.getRolesByUserId("USR123456"));

        assertEquals(
                "User not found with ID: USR123456",
                exception.getMessage());

        verify(userRoleRepository, never())
                .findByUserUserId(any());
    }

    @Test
    void removeRole_ShouldRemoveMappingSuccessfully() {

        when(userRoleRepository.findById("USRL123456"))
                .thenReturn(Optional.of(userRole));

        userRoleService.removeRole("USRL123456");

        verify(userRoleRepository).delete(userRole);
    }

    @Test
    void removeRole_ShouldThrowException_WhenMappingDoesNotExist() {

        when(userRoleRepository.findById("USRL123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.removeRole("USRL123456"));

        assertEquals(
                "UserRole not found with ID: USRL123456",
                exception.getMessage());

        verify(userRoleRepository, never()).delete(any());
    }
    
    @Test
    void getRolesByUserName_ShouldReturnUserRoles() {

        when(userRoleRepository.findByUserNameAndUserStatus(
                "Rohith", UserStatus.ACTIVE))
                .thenReturn(List.of(userRole));

        List<UserRoleResponseDTO> response =
                userRoleService.getRolesByUserName("Rohith");

        assertEquals(1, response.size());
        assertEquals("USR123456", response.get(0).getUserId());
        assertEquals("PLAYER", response.get(0).getRoleName());

        verify(userRoleRepository).findByUserNameAndUserStatus(
                "Rohith", UserStatus.ACTIVE);
    }

    @Test
    void getRolesByUserName_ShouldReturnEmptyList_WhenNoRolesFound() {

        when(userRoleRepository.findByUserNameAndUserStatus(
                "Unknown", UserStatus.ACTIVE))
                .thenReturn(List.of());

        List<UserRoleResponseDTO> response =
                userRoleService.getRolesByUserName("Unknown");

        assertEquals(0, response.size());

        verify(userRoleRepository).findByUserNameAndUserStatus(
                "Unknown", UserStatus.ACTIVE);
    }
    @Test
    void assignRole_ShouldThrowException_WhenUserIsInactive() {

        user.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRoleService.assignRole(requestDTO));

        assertEquals(
                "Cannot assign role to an inactive user",
                exception.getMessage());

        verify(roleRepository, never()).findById(any());
        verify(userRoleRepository, never()).save(any());
    }
}