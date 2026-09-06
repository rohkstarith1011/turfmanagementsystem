package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachingClassRegistrationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachingClassRegistrationResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.CoachingClass;
import com.crimsonlogic.turfmanagementsystem.entity.CoachingClassRegistration;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.CoachingClassRegistrationRepository;
import com.crimsonlogic.turfmanagementsystem.repository.CoachingClassRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICoachingClassRegistrationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoachingClassRegistrationServiceImpl
        implements ICoachingClassRegistrationService {

    private final CoachingClassRegistrationRepository registrationRepository;
    private final CoachingClassRepository coachingClassRepository;
    private final PlayerRepository playerRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public CoachingClassRegistrationServiceImpl(
            CoachingClassRegistrationRepository registrationRepository,
            CoachingClassRepository coachingClassRepository,
            PlayerRepository playerRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository) {

        this.registrationRepository = registrationRepository;
        this.coachingClassRepository = coachingClassRepository;
        this.playerRepository = playerRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    // =========================
    // CREATE REGISTRATION
    // =========================

    @Override
    @Transactional
    public CoachingClassRegistrationResponseDTO createRegistration(
            CoachingClassRegistrationRequestDTO requestDTO) {

        CoachingClass coachingClass = coachingClassRepository
                .findById(requestDTO.getCoachingClassId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Coaching class not found"));

        if (!"ACTIVE".equalsIgnoreCase(coachingClass.getStatus())) {
            throw new IllegalArgumentException(
                    "Coaching class must be ACTIVE");
        }

        Player player = playerRepository
                .findById(requestDTO.getPlayerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found"));

        if (player.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Player must be ACTIVE");
        }

        if (!hasActivePlayerRole(player.getUser().getUserId())) {
            throw new IllegalArgumentException(
                    "Player does not have an ACTIVE PLAYER role");
        }

        boolean alreadyRegistered =
                registrationRepository
                        .existsByCoachingClassCoachingClassIdAndPlayerPlayerId(
                                coachingClass.getCoachingClassId(),
                                player.getPlayerId());

        if (alreadyRegistered) {
            throw new IllegalArgumentException(
                    "Player is already registered for this coaching class");
        }

        long activeRegistrations =
                registrationRepository
                        .findByCoachingClassCoachingClassId(
                                coachingClass.getCoachingClassId())
                        .stream()
                        .filter(registration ->
                                "ACTIVE".equalsIgnoreCase(
                                        registration.getStatus()))
                        .count();

        if (activeRegistrations >=
                coachingClass.getRegistrationLimit()) {

            throw new IllegalArgumentException(
                    "Coaching class registration limit is full");
        }

        CoachingClassRegistration registration =
                new CoachingClassRegistration();

        registration.setCoachingClass(coachingClass);
        registration.setPlayer(player);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus("ACTIVE");

        CoachingClassRegistration savedRegistration =
                registrationRepository.save(registration);

        return mapToResponseDTO(savedRegistration);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public CoachingClassRegistrationResponseDTO getRegistrationById(
            String registrationId) {

        CoachingClassRegistration registration =
                registrationRepository.findById(registrationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Registration not found"));

        return mapToResponseDTO(registration);
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassRegistrationResponseDTO>
    getAllRegistrations() {

        return registrationRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // GET BY COACHING CLASS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassRegistrationResponseDTO>
    getRegistrationsByCoachingClass(String coachingClassId) {

        return registrationRepository
                .findByCoachingClassCoachingClassId(coachingClassId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // GET BY PLAYER
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassRegistrationResponseDTO>
    getRegistrationsByPlayer(String playerId) {

        return registrationRepository
                .findByPlayerPlayerId(playerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // SEARCH BY COACHING CLASS NAME
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassRegistrationResponseDTO>
    searchRegistrationsByCoachingClassName(String name) {

        return registrationRepository
                .findByCoachingClassNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // SEARCH BY PLAYER NAME
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<CoachingClassRegistrationResponseDTO>
    searchRegistrationsByPlayerName(String name) {

        return registrationRepository
                .findByPlayerNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // UPDATE REGISTRATION
    // =========================

    @Override
    @Transactional
    public CoachingClassRegistrationResponseDTO updateRegistration(
            String registrationId,
            CoachingClassRegistrationRequestDTO requestDTO) {

        CoachingClassRegistration registration =
                registrationRepository.findById(registrationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Registration not found"));

        // Registration cannot be moved to another class
        if (!registration.getCoachingClass()
                .getCoachingClassId()
                .equals(requestDTO.getCoachingClassId())) {

            throw new IllegalArgumentException(
                    "Coaching class cannot be changed");
        }

        // Registration cannot be assigned to another player
        if (!registration.getPlayer()
                .getPlayerId()
                .equals(requestDTO.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Player cannot be changed");
        }

        CoachingClass coachingClass =
                registration.getCoachingClass();

        Player player =
                registration.getPlayer();

        if (!"ACTIVE".equalsIgnoreCase(
                coachingClass.getStatus())) {

            throw new IllegalArgumentException(
                    "Coaching class must be ACTIVE");
        }

        if (player.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Player must be ACTIVE");
        }

        if (!hasActivePlayerRole(
                player.getUser().getUserId())) {

            throw new IllegalArgumentException(
                    "Player does not have an ACTIVE PLAYER role");
        }

        // Reactivate an inactive registration
        if ("INACTIVE".equalsIgnoreCase(
                registration.getStatus())) {

            long activeRegistrations =
                    registrationRepository
                            .findByCoachingClassCoachingClassId(
                                    coachingClass.getCoachingClassId())
                            .stream()
                            .filter(existing ->
                                    "ACTIVE".equalsIgnoreCase(
                                            existing.getStatus()))
                            .count();

            if (activeRegistrations >=
                    coachingClass.getRegistrationLimit()) {

                throw new IllegalArgumentException(
                        "Coaching class registration limit is full");
            }

            registration.setStatus("ACTIVE");
        }

        CoachingClassRegistration updatedRegistration =
                registrationRepository.save(registration);

        return mapToResponseDTO(updatedRegistration);
    }

    // =========================
    // DEACTIVATE
    // =========================

    @Override
    @Transactional
    public void deactivateRegistration(
            String registrationId) {

        CoachingClassRegistration registration =
                registrationRepository.findById(registrationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Registration not found"));

        if ("INACTIVE".equalsIgnoreCase(
                registration.getStatus())) {

            throw new IllegalArgumentException(
                    "Registration is already INACTIVE");
        }

        registration.setStatus("INACTIVE");

        registrationRepository.save(registration);
    }

    // =========================
    // CHECK ACTIVE PLAYER ROLE
    // =========================

    private boolean hasActivePlayerRole(String userId) {

        Role playerRole =
                roleRepository
                        .findByRoleName("PLAYER")
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "PLAYER role not found"));

        return userRoleRepository
                .findByUserUserId(userId)
                .stream()
                .anyMatch(userRole ->
                        userRole.getRole().getRoleId()
                                .equals(playerRole.getRoleId())
                        && userRole.getStatus() == UserStatus.ACTIVE);
    }

    // =========================
    // MAP ENTITY → RESPONSE DTO
    // =========================

    private CoachingClassRegistrationResponseDTO
    mapToResponseDTO(
            CoachingClassRegistration registration) {

        CoachingClassRegistrationResponseDTO response =
                new CoachingClassRegistrationResponseDTO();

        response.setCoachingClassRegistrationId(
                registration.getCoachingClassRegistrationId());

        response.setCoachingClassId(
                registration.getCoachingClass()
                        .getCoachingClassId());

        response.setPlayerId(
                registration.getPlayer()
                        .getPlayerId());

        response.setRegistrationDate(
                registration.getRegistrationDate());

        response.setStatus(
                registration.getStatus());

        return response;
    }
}