package com.crimsonlogic.turfmanagementsystem.service.impl;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO requestDTO;
    private User user;

    @BeforeEach
    void setUp() {

        requestDTO = new UserRequestDTO();

        requestDTO.setName("Rohith");
        requestDTO.setEmail("rohith@gmail.com");
        requestDTO.setPhone("9876543210");
        requestDTO.setPassword("Password123");

        user = new User();

        user.setUserId("USR123456");
        user.setName("Rohith");
        user.setEmail("rohith@gmail.com");
        user.setPhone("9876543210");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.ACTIVE);
    }

    // ---------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------

    @Test
    void createUser_ShouldCreateUserSuccessfully() {

        when(userRepository.existsByEmail(requestDTO.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByPhone(requestDTO.getPhone()))
                .thenReturn(false);

        when(passwordEncoder.encode(requestDTO.getPassword()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDTO response =
                userService.createUser(requestDTO);

        assertNotNull(response);
        assertEquals("USR123456", response.getUserId());
        assertEquals("Rohith", response.getName());
        assertEquals("rohith@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getPhone());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        verify(userRepository).existsByEmail(requestDTO.getEmail());
        verify(userRepository).existsByPhone(requestDTO.getPhone());
        verify(passwordEncoder).encode(requestDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(requestDTO.getEmail()))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createUser(requestDTO));

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository)
                .existsByEmail(requestDTO.getEmail());

        verify(userRepository, never())
                .existsByPhone(anyString());

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void createUser_ShouldThrowException_WhenPhoneAlreadyExists() {

        when(userRepository.existsByEmail(requestDTO.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByPhone(requestDTO.getPhone()))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createUser(requestDTO));

        assertEquals("Phone number already exists",
                exception.getMessage());

        verify(userRepository)
                .existsByEmail(requestDTO.getEmail());

        verify(userRepository)
                .existsByPhone(requestDTO.getPhone());

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    // ---------------------------------------------------------
    // GET ALL USERS
    // ---------------------------------------------------------

    @Test
    void getAllUsers_ShouldReturnAllUsers() {

        User secondUser = new User();

        secondUser.setUserId("USR654321");
        secondUser.setName("Rahul");
        secondUser.setEmail("rahul@gmail.com");
        secondUser.setPhone("9123456780");
        secondUser.setPassword("encodedPassword");
        secondUser.setStatus(UserStatus.ACTIVE);

        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user, secondUser));

        List<UserResponseDTO> response =
                userService.getAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("USR123456",
                response.get(0).getUserId());

        assertEquals("USR654321",
                response.get(1).getUserId());

        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {

        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<UserResponseDTO> response =
                userService.getAllUsers();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(userRepository).findAll();
    }

    // ---------------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------------

    @Test
    void getUserById_ShouldReturnUserSuccessfully() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        UserResponseDTO response =
                userService.getUserById("USR123456");

        assertNotNull(response);
        assertEquals("USR123456", response.getUserId());
        assertEquals("Rohith", response.getName());
        assertEquals("rohith@gmail.com", response.getEmail());

        verify(userRepository).findById("USR123456");
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById("USR999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.getUserById("USR999999"));

        assertEquals(
                "User not found with ID: USR999999",
                exception.getMessage());

        verify(userRepository).findById("USR999999");
    }

    // ---------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByEmail("newemail@gmail.com"))
                .thenReturn(false);

        when(userRepository.existsByPhone("9123456789"))
                .thenReturn(false);

        when(passwordEncoder.encode("NewPassword123"))
                .thenReturn("newEncodedPassword");

        requestDTO.setEmail("newemail@gmail.com");
        requestDTO.setPhone("9123456789");
        requestDTO.setPassword("NewPassword123");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDTO response =
                userService.updateUser("USR123456", requestDTO);

        assertNotNull(response);

        verify(userRepository)
                .findById("USR123456");

        verify(userRepository)
                .existsByEmail("newemail@gmail.com");

        verify(userRepository)
                .existsByPhone("9123456789");

        verify(passwordEncoder)
                .encode("NewPassword123");

        verify(userRepository)
                .save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById("USR999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.updateUser(
                                "USR999999",
                                requestDTO));

        assertEquals(
                "User not found with ID: USR999999",
                exception.getMessage());

        verify(userRepository)
                .findById("USR999999");

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        requestDTO.setEmail("existing@gmail.com");

        when(userRepository.existsByEmail("existing@gmail.com"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.updateUser(
                                "USR123456",
                                requestDTO));

        assertEquals(
                "Email already exists",
                exception.getMessage());

        verify(userRepository)
                .existsByEmail("existing@gmail.com");

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenPhoneAlreadyExists() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        requestDTO.setPhone("9999999999");

        when(userRepository.existsByPhone("9999999999"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.updateUser(
                                "USR123456",
                                requestDTO));

        assertEquals(
                "Phone number already exists",
                exception.getMessage());

        verify(userRepository)
                .existsByPhone("9999999999");

        verify(userRepository, never())
                .save(any(User.class));
    }

    // ---------------------------------------------------------
    // DEACTIVATE USER
    // ---------------------------------------------------------

    @Test
    void deactivateUser_ShouldDeactivateUserSuccessfully() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        userService.deactivateUser("USR123456");

        assertEquals(UserStatus.INACTIVE,
                user.getStatus());

        verify(userRepository)
                .findById("USR123456");

        verify(userRepository)
                .save(user);
    }

    @Test
    void deactivateUser_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById("USR999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.deactivateUser(
                                "USR999999"));

        assertEquals(
                "User not found with ID: USR999999",
                exception.getMessage());

        verify(userRepository)
                .findById("USR999999");

        verify(userRepository, never())
                .save(any(User.class));
    }
}