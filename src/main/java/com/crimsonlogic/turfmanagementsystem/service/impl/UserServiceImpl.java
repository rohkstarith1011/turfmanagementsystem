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
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserService;


@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByPhone(requestDTO.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = new User();

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

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
                        new IllegalArgumentException(
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
                        new IllegalArgumentException(
                                "User not found with ID: " + userId));

        if (!user.getEmail().equals(requestDTO.getEmail())
                && userRepository.existsByEmail(requestDTO.getEmail())) {

            throw new IllegalArgumentException("Email already exists");
        }

        if (!user.getPhone().equals(requestDTO.getPhone())
                && userRepository.existsByPhone(requestDTO.getPhone())) {

            throw new IllegalArgumentException(
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
                        new IllegalArgumentException(
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
