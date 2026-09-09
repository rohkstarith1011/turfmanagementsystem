package com.crimsonlogic.turfmanagementsystem.service.impl;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.exception.BadRequestException;
import com.crimsonlogic.turfmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserService;


@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PlayerRepository playerRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           PlayerRepository playerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByPhone(requestDTO.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }

        User user = new User();

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        // Automatically assign the PLAYER role to the new user
        roleRepository.findByRoleName("PLAYER").ifPresent(playerRole -> {
            UserRole userRole = new UserRole();
            userRole.setUser(savedUser);
            userRole.setRole(playerRole);
            userRole.setStatus(UserStatus.ACTIVE);
            userRoleRepository.save(userRole);
        });

        // Automatically create a default Player profile
        Player player = new Player();
        player.setUser(savedUser);
        player.setName(savedUser.getName());
        player.setEmail(savedUser.getEmail());
        player.setPhone(savedUser.getPhone());
        player.setStatus(UserStatus.ACTIVE);
        // Default values for other fields
        player.setLocality("Unknown");
        player.setSkillLevel("BEGINNER");
        player.setPreferredSports("None");
        playerRepository.save(player);

        return mapToResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId));

        return mapToResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(
            String userId,
            UserRequestDTO requestDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId));

        if (!user.getEmail().equals(requestDTO.getEmail())
                && userRepository.existsByEmail(requestDTO.getEmail())) {

            throw new BadRequestException("Email already exists");
        }

        if (!user.getPhone().equals(requestDTO.getPhone())
                && userRepository.existsByPhone(requestDTO.getPhone())) {

            throw new BadRequestException(
                    "Phone number already exists");
        }

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setPassword(
                passwordEncoder.encode(requestDTO.getPassword()));

        User updatedUser = userRepository.save(user);

        return mapToResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deactivateUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId));

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
    }

    private UserResponseDTO mapToResponseDTO(User user) {

        UserResponseDTO responseDTO = new UserResponseDTO();

        responseDTO.setUserId(user.getUserId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setPhone(user.getPhone());
        responseDTO.setStatus(user.getStatus());

        return responseDTO;
    }
}
