package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TurfManagerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfManagerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.TurfManager;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.TurfManagerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TurfManagerServiceImpl implements ITurfManagerService {

    private final TurfManagerRepository turfManagerRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public TurfManagerServiceImpl(
            TurfManagerRepository turfManagerRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository) {

        this.turfManagerRepository = turfManagerRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public TurfManagerResponseDTO createTurfManager(
            TurfManagerRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot create Turf Manager profile for an inactive User");
        }

        // Verify that the User has the MANAGER role
        if (!hasManagerRole(requestDTO.getUserId())) {
            throw new IllegalArgumentException(
                    "User does not have the MANAGER role");
        }

        Optional<TurfManager> existingTurfManager =
                turfManagerRepository.findByUserUserId(requestDTO.getUserId());

        if (existingTurfManager.isPresent()) {

            TurfManager existingManager = existingTurfManager.get();

            // Existing profile is inactive → reactivate it
            if (existingManager.getStatus() == UserStatus.INACTIVE) {

                existingManager.setStatus(UserStatus.ACTIVE);

                TurfManager reactivatedManager =
                        turfManagerRepository.save(existingManager);

                return mapToResponseDTO(reactivatedManager);
            }

            // Existing profile is already active → duplicate
            throw new IllegalArgumentException(
                    "Turf Manager profile already exists for this User");
        }

        if (!user.getName().equals(requestDTO.getName())
                || !user.getEmail().equals(requestDTO.getEmail())
                || !user.getPhone().equals(requestDTO.getPhone())) {

            throw new IllegalArgumentException(
                    "Turf Manager details must match the linked User");
        }

        TurfManager turfManager = new TurfManager();

        turfManager.setUser(user);
        turfManager.setName(user.getName());
        turfManager.setEmail(user.getEmail());
        turfManager.setPhone(user.getPhone());
        turfManager.setStatus(UserStatus.ACTIVE);

        TurfManager savedTurfManager =
                turfManagerRepository.save(turfManager);

        return mapToResponseDTO(savedTurfManager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfManagerResponseDTO> getAllTurfManagers() {

        return turfManagerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TurfManagerResponseDTO getTurfManagerById(
            String turfManagerId) {

        TurfManager turfManager =
                turfManagerRepository.findById(turfManagerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Manager not found"));

        return mapToResponseDTO(turfManager);
    }

    @Override
    @Transactional(readOnly = true)
    public TurfManagerResponseDTO getTurfManagerByUserId(
            String userId) {

        TurfManager turfManager =
                turfManagerRepository.findByUserUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Manager profile not found for this User"));

        return mapToResponseDTO(turfManager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurfManagerResponseDTO> getTurfManagersByName(
            String name) {

        return turfManagerRepository.findAll()
                .stream()
                .filter(manager ->
                        manager.getName() != null
                                && manager.getName().equalsIgnoreCase(name))
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public TurfManagerResponseDTO updateTurfManager(
            String turfManagerId,
            TurfManagerRequestDTO requestDTO) {

        TurfManager turfManager =
                turfManagerRepository.findById(turfManagerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Manager not found"));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!turfManager.getUser().getUserId()
                .equals(requestDTO.getUserId())) {

            throw new IllegalArgumentException(
                    "Turf Manager cannot be reassigned to another User");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot update Turf Manager profile for an inactive User");
        }

        // Verify that the User still has the MANAGER role
        if (!hasManagerRole(requestDTO.getUserId())) {
            throw new IllegalArgumentException(
                    "User does not have the MANAGER role");
        }

 

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());

        turfManager.setName(requestDTO.getName());
        turfManager.setEmail(requestDTO.getEmail());
        turfManager.setPhone(requestDTO.getPhone());

        userRepository.save(user);

        TurfManager updatedTurfManager =
                turfManagerRepository.save(turfManager);

        return mapToResponseDTO(updatedTurfManager);
    }

    @Override
    @Transactional
    public void deactivateTurfManager(String turfManagerId) {

        TurfManager turfManager =
                turfManagerRepository.findById(turfManagerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Turf Manager not found"));

        if (turfManager.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException(
                    "Turf Manager is already inactive");
        }

        turfManager.setStatus(UserStatus.INACTIVE);
        turfManagerRepository.save(turfManager);

        userRoleRepository.findByUserUserId(
                turfManager.getUser().getUserId())
                .stream()
                .filter(userRole ->
                        "MANAGER".equalsIgnoreCase(
                                userRole.getRole().getRoleName()))
                .forEach(userRole -> {
                    userRole.setStatus(UserStatus.INACTIVE);
                    userRoleRepository.save(userRole);
                });
    }

    private boolean hasManagerRole(String userId) {

        return userRoleRepository
                .findByUserUserId(userId)
                .stream()
                .anyMatch(userRole ->
                        "MANAGER".equalsIgnoreCase(
                                userRole.getRole().getRoleName())
                        && userRole.getStatus() == UserStatus.ACTIVE);
    }

    private TurfManagerResponseDTO mapToResponseDTO(
            TurfManager turfManager) {

        TurfManagerResponseDTO responseDTO =
                new TurfManagerResponseDTO();

        responseDTO.setTurfManagerId(
                turfManager.getTurfManagerId());

        responseDTO.setUserId(
                turfManager.getUser().getUserId());

        responseDTO.setName(turfManager.getName());
        responseDTO.setEmail(turfManager.getEmail());
        responseDTO.setPhone(turfManager.getPhone());
        responseDTO.setStatus(turfManager.getStatus());

        return responseDTO;
    }
}