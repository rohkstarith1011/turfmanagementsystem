package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfOwnerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.TurfOwner;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfOwnerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TurfOwnerServiceImpl implements ITurfOwnerService {

    private final TurfOwnerRepository turfOwnerRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public TurfOwnerServiceImpl(
            TurfOwnerRepository turfOwnerRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository) {

        this.turfOwnerRepository = turfOwnerRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public TurfOwnerResponseDTO createTurfOwner(TurfOwnerRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active users can become turf owners");
        }

        boolean hasOwnerRole = userRoleRepository.findByUserUserId(user.getUserId())
                .stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .anyMatch("OWNER"::equalsIgnoreCase);

        if (!hasOwnerRole) {
            throw new IllegalArgumentException("User does not have OWNER role");
        }

        Optional<TurfOwner> existingTurfOwner =
                turfOwnerRepository.findByUserUserId(user.getUserId());

        if (existingTurfOwner.isPresent()) {

            TurfOwner existingOwner = existingTurfOwner.get();

            // Existing profile is inactive → reactivate it
            if (existingOwner.getStatus() == UserStatus.INACTIVE) {

                existingOwner.setStatus(UserStatus.ACTIVE);

                TurfOwner reactivatedOwner =
                        turfOwnerRepository.save(existingOwner);

                return mapToResponseDTO(reactivatedOwner);
            }

            // Existing profile is already active → duplicate
            throw new IllegalArgumentException(
                    "Turf owner profile already exists for this user");
        }

        if (!user.getName().equals(requestDTO.getName())
                || !user.getEmail().equals(requestDTO.getEmail())
                || !user.getPhone().equals(requestDTO.getPhone())) {

            throw new IllegalArgumentException(
                    "Turf owner details must match the linked user details");
        }

        TurfOwner turfOwner = new TurfOwner();

        turfOwner.setUser(user);
        turfOwner.setName(user.getName());
        turfOwner.setEmail(user.getEmail());
        turfOwner.setPhone(user.getPhone());
        turfOwner.setStatus(UserStatus.ACTIVE);

        TurfOwner savedTurfOwner = turfOwnerRepository.save(turfOwner);

        return mapToResponseDTO(savedTurfOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfOwnerResponseDTO> getTurfOwnersByName(String name) {

        return turfOwnerRepository.findByNameIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TurfOwnerResponseDTO> getAllTurfOwners() {

        return turfOwnerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TurfOwnerResponseDTO getTurfOwnerById(String turfOwnerId) {

        TurfOwner turfOwner = turfOwnerRepository.findById(turfOwnerId)
                .orElseThrow(() -> new IllegalArgumentException("Turf owner not found"));

        return mapToResponseDTO(turfOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public TurfOwnerResponseDTO getTurfOwnerByUserId(String userId) {

        TurfOwner turfOwner = turfOwnerRepository.findByUserUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Turf owner profile not found for user"));

        return mapToResponseDTO(turfOwner);
    }

    @Override
    @Transactional
    public TurfOwnerResponseDTO updateTurfOwner(
            String turfOwnerId,
            TurfOwnerRequestDTO requestDTO) {

        TurfOwner turfOwner = turfOwnerRepository.findById(turfOwnerId)
                .orElseThrow(() -> new IllegalArgumentException("Turf owner not found"));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!turfOwner.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException(
                    "Turf owner profile cannot be reassigned to another user");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active users can be turf owners");
        }

        boolean hasOwnerRole = userRoleRepository.findByUserUserId(user.getUserId())
                .stream()
                .anyMatch(userRole ->
                        "OWNER".equalsIgnoreCase(
                                userRole.getRole().getRoleName())
                        && userRole.getStatus() == UserStatus.ACTIVE);

        if (!hasOwnerRole) {
            throw new IllegalArgumentException("User does not have OWNER role");
        }

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());

        turfOwner.setName(user.getName());
        turfOwner.setEmail(user.getEmail());
        turfOwner.setPhone(user.getPhone());

        User updatedUser = userRepository.save(user);

        turfOwner.setUser(updatedUser);

        TurfOwner updatedTurfOwner = turfOwnerRepository.save(turfOwner);

        return mapToResponseDTO(updatedTurfOwner);
    }

    @Override
    @Transactional
    public void deactivateTurfOwner(String turfOwnerId) {

        TurfOwner turfOwner = turfOwnerRepository.findById(turfOwnerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Turf owner not found"));

        if (turfOwner.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException(
                    "Turf owner is already inactive");
        }

        turfOwner.setStatus(UserStatus.INACTIVE);
        turfOwnerRepository.save(turfOwner);

        userRoleRepository.findByUserUserId(
                turfOwner.getUser().getUserId())
                .stream()
                .filter(userRole ->
                        "OWNER".equalsIgnoreCase(
                                userRole.getRole().getRoleName()))
                .forEach(userRole -> {
                    userRole.setStatus(UserStatus.INACTIVE);
                    userRoleRepository.save(userRole);
                });
    }

    private TurfOwnerResponseDTO mapToResponseDTO(TurfOwner turfOwner) {

        TurfOwnerResponseDTO responseDTO = new TurfOwnerResponseDTO();

        responseDTO.setTurfOwnerId(turfOwner.getTurfOwnerId());
        responseDTO.setUserId(turfOwner.getUser().getUserId());
        responseDTO.setName(turfOwner.getName());
        responseDTO.setEmail(turfOwner.getEmail());
        responseDTO.setPhone(turfOwner.getPhone());
        responseDTO.setStatus(turfOwner.getStatus());

        return responseDTO;
    }
}