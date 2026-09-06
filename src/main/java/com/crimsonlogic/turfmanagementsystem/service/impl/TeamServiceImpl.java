package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TeamRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TeamResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Sport;
import com.crimsonlogic.turfmanagementsystem.entity.Team;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SportRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TeamRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITeamService;

@Service
@Transactional
public class TeamServiceImpl implements ITeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final SportRepository sportRepository;

    public TeamServiceImpl(
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            SportRepository sportRepository) {

        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.sportRepository = sportRepository;
    }

    @Override
    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {

        Player player = playerRepository.findById(requestDTO.getCreatedBy())
                .orElseThrow(() ->
                        new IllegalArgumentException("Player not found"));

        if (player.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot create Team using an inactive Player");
        }

        Sport sport = sportRepository.findById(requestDTO.getSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Sport not found"));

        if (!"ACTIVE".equalsIgnoreCase(sport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot create Team for an inactive Sport");
        }

        Team team = new Team();

        team.setName(requestDTO.getName());
        team.setDescription(requestDTO.getDescription());
        team.setSport(sport);
        team.setCreatedBy(player);
        team.setStatus("ACTIVE");
        team.setCreatedAt(LocalDateTime.now());

        return mapToResponseDTO(teamRepository.save(team));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDTO getTeamById(String teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Team not found"));

        return mapToResponseDTO(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponseDTO> getAllTeams() {

        return teamRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponseDTO> getTeamsByPlayer(String playerId) {

        return teamRepository.findByCreatedByPlayerId(playerId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponseDTO> getTeamsBySport(String sportId) {

        return teamRepository.findBySportSportId(sportId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponseDTO> getTeamsByName(String name) {

        return teamRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeamResponseDTO updateTeam(
            String teamId,
            TeamRequestDTO requestDTO) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Team not found"));

        Player player = playerRepository.findById(requestDTO.getCreatedBy())
                .orElseThrow(() ->
                        new IllegalArgumentException("Player not found"));

        if (!team.getCreatedBy().getPlayerId()
                .equals(player.getPlayerId())) {

            throw new IllegalArgumentException(
                    "Team cannot be reassigned to another Player");
        }

        if (player.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot update Team using an inactive Player");
        }

        Sport sport = sportRepository.findById(requestDTO.getSportId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Sport not found"));

        if (!"ACTIVE".equalsIgnoreCase(sport.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot update Team with an inactive Sport");
        }

        team.setName(requestDTO.getName());
        team.setDescription(requestDTO.getDescription());
        team.setSport(sport);

        if ("INACTIVE".equalsIgnoreCase(team.getStatus())) {
            team.setStatus("ACTIVE");
        }

        return mapToResponseDTO(teamRepository.save(team));
    }

    @Override
    public void deactivateTeam(String teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Team not found"));

        if ("INACTIVE".equalsIgnoreCase(team.getStatus())) {
            throw new IllegalArgumentException(
                    "Team is already inactive");
        }

        team.setStatus("INACTIVE");

        teamRepository.save(team);
    }

    private TeamResponseDTO mapToResponseDTO(Team team) {

        TeamResponseDTO responseDTO = new TeamResponseDTO();

        responseDTO.setTeamId(team.getTeamId());
        responseDTO.setName(team.getName());
        responseDTO.setDescription(team.getDescription());
        responseDTO.setSportId(team.getSport().getSportId());
        responseDTO.setCreatedBy(team.getCreatedBy().getPlayerId());
        responseDTO.setStatus(team.getStatus());
        responseDTO.setCreatedAt(team.getCreatedAt());

        return responseDTO;
    }
}