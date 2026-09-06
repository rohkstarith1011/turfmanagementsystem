package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PlayerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IPlayerService;

@Service
@Transactional
public class PlayerServiceImpl implements IPlayerService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository) {

        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public PlayerResponseDTO createPlayer(PlayerRequestDTO requestDTO) {

        User user = getActiveUser(requestDTO.getUserId());

        validatePlayerRole(user.getUserId());

        validateUserDetails(user, requestDTO);

        Player existingPlayer = playerRepository
                .findByUserUserId(user.getUserId())
                .orElse(null);

        if (existingPlayer != null) {

            if (existingPlayer.getStatus() == UserStatus.ACTIVE) {
                throw new IllegalArgumentException(
                        "Player profile already exists for this user");
            }

            existingPlayer.setName(requestDTO.getName());
            existingPlayer.setEmail(requestDTO.getEmail());
            existingPlayer.setPhone(requestDTO.getPhone());
            existingPlayer.setLocality(requestDTO.getLocality());
            existingPlayer.setSkillLevel(requestDTO.getSkillLevel());
            existingPlayer.setPreferredSports(
                    requestDTO.getPreferredSports());
            existingPlayer.setStatus(UserStatus.ACTIVE);

            return mapToResponseDTO(
                    playerRepository.save(existingPlayer));
        }

        Player player = new Player();

        player.setUser(user);
        player.setName(requestDTO.getName());
        player.setEmail(requestDTO.getEmail());
        player.setPhone(requestDTO.getPhone());
        player.setLocality(requestDTO.getLocality());
        player.setSkillLevel(requestDTO.getSkillLevel());
        player.setPreferredSports(requestDTO.getPreferredSports());
        player.setStatus(UserStatus.ACTIVE);

        return mapToResponseDTO(
                playerRepository.save(player));
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponseDTO getPlayerById(String playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found"));

        return mapToResponseDTO(player);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponseDTO> getAllPlayers() {

        return playerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResponseDTO updatePlayer(
            String playerId,
            PlayerRequestDTO requestDTO) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found"));

        User user = getActiveUser(requestDTO.getUserId());

        if (!player.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new IllegalArgumentException(
                    "Player cannot be reassigned to another user");
        }

        validatePlayerRole(user.getUserId());

        validateUserDetails(user, requestDTO);

        player.setName(requestDTO.getName());
        player.setEmail(requestDTO.getEmail());
        player.setPhone(requestDTO.getPhone());
        player.setLocality(requestDTO.getLocality());
        player.setSkillLevel(requestDTO.getSkillLevel());
        player.setPreferredSports(
                requestDTO.getPreferredSports());

        if (player.getStatus() == UserStatus.INACTIVE) {
            player.setStatus(UserStatus.ACTIVE);
        }

        return mapToResponseDTO(
                playerRepository.save(player));
    }

    @Override
    public void deactivatePlayer(String playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found"));

        if (player.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException(
                    "Player is already inactive");
        }

        player.setStatus(UserStatus.INACTIVE);

        playerRepository.save(player);
    }

    private User getActiveUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "User is inactive");
        }

        return user;
    }

    private void validatePlayerRole(String userId) {

        Role playerRole = roleRepository
                .findByRoleName("PLAYER")
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "PLAYER role not found"));

        List<UserRole> userRoles =
                userRoleRepository.findByUserUserId(userId);

        boolean hasActivePlayerRole = userRoles
                .stream()
                .anyMatch(userRole ->
                        userRole.getRole().getRoleId()
                                .equals(playerRole.getRoleId())
                        && userRole.getStatus() == UserStatus.ACTIVE);

        if (!hasActivePlayerRole) {
            throw new IllegalArgumentException(
                    "User does not have an active PLAYER role");
        }
    }

    private void validateUserDetails(
            User user,
            PlayerRequestDTO requestDTO) {

        if (!user.getName().equals(requestDTO.getName())) {
            throw new IllegalArgumentException(
                    "Player name must match user name");
        }

        if (!user.getEmail().equalsIgnoreCase(
                requestDTO.getEmail())) {

            throw new IllegalArgumentException(
                    "Player email must match user email");
        }

        if (!user.getPhone().equals(requestDTO.getPhone())) {
            throw new IllegalArgumentException(
                    "Player phone must match user phone");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponseDTO> getPlayersByName(String name) {

        return playerRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    private PlayerResponseDTO mapToResponseDTO(Player player) {

        PlayerResponseDTO responseDTO = new PlayerResponseDTO();

        responseDTO.setPlayerId(player.getPlayerId());
        responseDTO.setUserId(
                player.getUser().getUserId());
        responseDTO.setName(player.getName());
        responseDTO.setEmail(player.getEmail());
        responseDTO.setPhone(player.getPhone());
        responseDTO.setStatus(player.getStatus());
        responseDTO.setLocality(player.getLocality());
        responseDTO.setSkillLevel(player.getSkillLevel());
        responseDTO.setPreferredSports(
                player.getPreferredSports());

        return responseDTO;
    }
}