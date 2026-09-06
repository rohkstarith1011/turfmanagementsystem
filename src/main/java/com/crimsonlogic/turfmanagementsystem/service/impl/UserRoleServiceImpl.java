package com.crimsonlogic.turfmanagementsystem.service.impl;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRoleRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserRoleResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IUserRoleService;

@Service
public class UserRoleServiceImpl implements IUserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(
            UserRoleRepository userRoleRepository,
            UserRepository userRepository,
            RoleRepository roleRepository) {

        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserRoleResponseDTO assignRole(UserRoleRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with ID: " + requestDTO.getUserId()));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot assign role to an inactive user");
        }

        Role role = roleRepository.findById(requestDTO.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Role not found with ID: " + requestDTO.getRoleId()));

        List<UserRole> existingUserRoles =
                userRoleRepository.findByUserUserId(requestDTO.getUserId());

        for (UserRole existingUserRole : existingUserRoles) {

            if (existingUserRole.getRole().getRoleId()
                    .equals(requestDTO.getRoleId())) {

                // Existing role mapping is ACTIVE
                if (existingUserRole.getStatus() == UserStatus.ACTIVE
                        || existingUserRole.getStatus() == null) {

                    // Handles old records created before status was introduced
                    if (existingUserRole.getStatus() == null) {
                        existingUserRole.setStatus(UserStatus.ACTIVE);
                        userRoleRepository.save(existingUserRole);
                    }

                    throw new IllegalArgumentException(
                            "Role is already assigned to this user");
                }

                // Existing role mapping is INACTIVE
                existingUserRole.setStatus(UserStatus.ACTIVE);

                UserRole reactivatedUserRole =
                        userRoleRepository.save(existingUserRole);

                return mapToResponseDTO(reactivatedUserRole);
            }
        }

        // No existing mapping → create a new one
        UserRole userRole = new UserRole();

        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setStatus(UserStatus.ACTIVE);

        UserRole savedUserRole =
                userRoleRepository.save(userRole);

        return mapToResponseDTO(savedUserRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getAllUserRoles() {

        return userRoleRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserRoleResponseDTO getUserRoleById(String userRoleId) {

        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "UserRole not found with ID: " + userRoleId));

        return mapToResponseDTO(userRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getRolesByUserId(String userId) {

        // Verify that the user actually exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(
                    "User not found with ID: " + userId);
        }

        return userRoleRepository.findByUserUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeRole(String userRoleId) {

        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "UserRole not found with ID: " + userRoleId));

        userRoleRepository.delete(userRole);
    }

    private UserRoleResponseDTO mapToResponseDTO(UserRole userRole) {

        UserRoleResponseDTO responseDTO = new UserRoleResponseDTO();

        responseDTO.setUserRoleId(userRole.getUserRoleId());
        responseDTO.setUserId(userRole.getUser().getUserId());
        responseDTO.setRoleId(userRole.getRole().getRoleId());
        responseDTO.setRoleName(userRole.getRole().getRoleName());
        responseDTO.setStatus(userRole.getStatus());

        return responseDTO;
    }
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getRolesByUserName(String name) {

        return userRoleRepository
                .findByUserNameAndUserStatus(name, UserStatus.ACTIVE)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}