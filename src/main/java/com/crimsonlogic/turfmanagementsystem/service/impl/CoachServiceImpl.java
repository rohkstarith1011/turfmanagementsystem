package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CoachRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CoachResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Coach;
import com.crimsonlogic.turfmanagementsystem.entity.TurfSport;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.CoachRepository;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfSportRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICoachService;

@Service
@Transactional
public class CoachServiceImpl implements ICoachService {

    private final CoachRepository coachRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final TurfSportRepository turfSportRepository;

    public CoachServiceImpl(
            CoachRepository coachRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository,
            TurfSportRepository turfSportRepository) {

        this.coachRepository = coachRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.turfSportRepository = turfSportRepository;
    }

    @Override
    public CoachResponseDTO createCoach(CoachRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getStatus() == null
                || !"ACTIVE".equalsIgnoreCase(user.getStatus().name())) {
            throw new IllegalArgumentException(
                    "Cannot create Coach profile for an inactive User");
        }

        if (!hasActiveCoachRole(user.getUserId())) {
            throw new IllegalArgumentException(
                    "User does not have an active COACH role");
        }

        validateUserDetails(user, requestDTO);

        TurfSport turfSport = turfSportRepository
                .findById(requestDTO.getTurfSportId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "TurfSport not found"));

        if (!"ACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign Coach to an inactive TurfSport");
        }

        Coach existingCoach = coachRepository
                .findByUserUserId(requestDTO.getUserId())
                .orElse(null);

        if (existingCoach != null) {

            if ("ACTIVE".equalsIgnoreCase(existingCoach.getStatus().name())) {
                throw new IllegalArgumentException(
                        "Coach profile already exists for this user");
            }

            existingCoach.setName(user.getName());
            existingCoach.setEmail(user.getEmail());
            existingCoach.setPhone(user.getPhone());
            existingCoach.setSpecialization(requestDTO.getSpecialization());
            existingCoach.setTurfSport(turfSport);
            existingCoach.setStatus(UserStatus.ACTIVE);

            Coach saved = coachRepository.save(existingCoach);
            return mapToResponseDTO(saved);
        }

        Coach coach = new Coach();

        coach.setUser(user);
        coach.setName(user.getName());
        coach.setEmail(user.getEmail());
        coach.setPhone(user.getPhone());
        coach.setTurfSport(turfSport);
        coach.setSpecialization(requestDTO.getSpecialization());
        coach.setStatus(UserStatus.ACTIVE);

        Coach saved = coachRepository.save(coach);

        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CoachResponseDTO getCoachById(String coachId) {

        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Coach not found"));

        return mapToResponseDTO(coach);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachResponseDTO> getAllCoaches() {

        return coachRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachResponseDTO> getCoachesByTurfSport(String turfSportId) {

        if (!turfSportRepository.existsById(turfSportId)) {
            throw new IllegalArgumentException("TurfSport not found");
        }

        return coachRepository.findByTurfSportTurfSportId(turfSportId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public CoachResponseDTO updateCoach(
            String coachId,
            CoachRequestDTO requestDTO) {

        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Coach not found"));

        if (!coach.getUser().getUserId()
                .equals(requestDTO.getUserId())) {

            throw new IllegalArgumentException(
                    "Coach cannot be reassigned to another User");
        }

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found"));

        if (user.getStatus() == null
                || !"ACTIVE".equalsIgnoreCase(user.getStatus().name())) {

            throw new IllegalArgumentException(
                    "Cannot update Coach for an inactive User");
        }

        if (!hasActiveCoachRole(user.getUserId())) {
            throw new IllegalArgumentException(
                    "User does not have an active COACH role");
        }

        validateUserDetails(user, requestDTO);

        TurfSport turfSport = turfSportRepository
                .findById(requestDTO.getTurfSportId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "TurfSport not found"));

        if (!"ACTIVE".equalsIgnoreCase(turfSport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign Coach to an inactive TurfSport");
        }

        coach.setName(user.getName());
        coach.setEmail(user.getEmail());
        coach.setPhone(user.getPhone());
        coach.setSpecialization(requestDTO.getSpecialization());
        coach.setTurfSport(turfSport);

        if ("INACTIVE".equalsIgnoreCase(coach.getStatus().name())) {
            coach.setStatus(
                    UserStatus.ACTIVE);
        }

        Coach saved = coachRepository.save(coach);

        return mapToResponseDTO(saved);
    }

    @Override
    public void deactivateCoach(String coachId) {

        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Coach not found"));

        if ("INACTIVE".equalsIgnoreCase(coach.getStatus().name())) {
            throw new IllegalArgumentException(
                    "Coach is already inactive");
        }

        coach.setStatus(
                UserStatus.INACTIVE);

        coachRepository.save(coach);

        String coachRoleId = roleRepository
                .findByRoleName("COACH")
                .orElseThrow(() -> new IllegalArgumentException(
                        "COACH role not found"))
                .getRoleId();

        UserRole userRole = userRoleRepository
                .findByUserUserId(coach.getUser().getUserId())
                .stream()
                .filter(ur -> ur.getRole().getRoleId().equals(coachRoleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "COACH UserRole mapping not found"));

        userRole.setStatus(
                UserStatus.INACTIVE);

        userRoleRepository.save(userRole);
    }

    private boolean hasActiveCoachRole(String userId) {

        return userRoleRepository.findByUserUserId(userId)
                .stream()
                .anyMatch(userRole ->
                        "COACH".equalsIgnoreCase(
                                userRole.getRole().getRoleName())
                        && userRole.getStatus() != null
                        && "ACTIVE".equalsIgnoreCase(
                                userRole.getStatus().name()));
    }

    private void validateUserDetails(
            User user,
            CoachRequestDTO requestDTO) {

        if (!user.getName().equals(requestDTO.getName())
                || !user.getEmail().equals(requestDTO.getEmail())
                || !user.getPhone().equals(requestDTO.getPhone())) {

            throw new IllegalArgumentException(
                    "Coach details must match the User details");
        }
    }

    private CoachResponseDTO mapToResponseDTO(Coach coach) {

        CoachResponseDTO response = new CoachResponseDTO();

        response.setCoachId(coach.getCoachId());
        response.setUserId(coach.getUser().getUserId());
        response.setName(coach.getName());
        response.setEmail(coach.getEmail());
        response.setPhone(coach.getPhone());
        response.setStatus(coach.getStatus());
        response.setTurfSportId(
                coach.getTurfSport().getTurfSportId());
        response.setSpecialization(
                coach.getSpecialization());

        return response;
    }
}