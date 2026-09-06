package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TeamPlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TeamPlayerResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Player;
import com.crimsonlogic.turfmanagementsystem.entity.Team;
import com.crimsonlogic.turfmanagementsystem.entity.TeamPlayer;
import com.crimsonlogic.turfmanagementsystem.repository.PlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TeamPlayerRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TeamRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITeamPlayerService;

@Service
@Transactional
public class TeamPlayerServiceImpl implements ITeamPlayerService {

    private final TeamPlayerRepository teamPlayerRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamPlayerServiceImpl(
            TeamPlayerRepository teamPlayerRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository) {

        this.teamPlayerRepository = teamPlayerRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public TeamPlayerResponseDTO addPlayerToTeam(
            TeamPlayerRequestDTO requestDTO) {

        Team team = teamRepository.findById(requestDTO.getTeamId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Team not found"));

        if (!"ACTIVE".equalsIgnoreCase(team.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot add player to an inactive Team");
        }

        Player player = playerRepository.findById(requestDTO.getPlayerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Player not found"));

        if (player.getStatus() == null
                || !player.getStatus().name().equalsIgnoreCase("ACTIVE")) {

            throw new IllegalArgumentException(
                    "Cannot add an inactive Player to a Team");
        }

        /*
         * Existing membership:
         * ACTIVE   -> already a member
         * INACTIVE -> reactivate same membership
         */
        TeamPlayer existing =
                teamPlayerRepository
                        .findByTeamTeamIdAndPlayerPlayerId(
                                requestDTO.getTeamId(),
                                requestDTO.getPlayerId())
                        .orElse(null);

        if (existing != null) {

            if ("ACTIVE".equalsIgnoreCase(existing.getStatus())) {
                throw new IllegalArgumentException(
                        "Player is already a member of this Team");
            }

            existing.setStatus("ACTIVE");

            TeamPlayer saved =
                    teamPlayerRepository.save(existing);

            return mapToResponseDTO(saved);
        }

        TeamPlayer teamPlayer = new TeamPlayer();

        teamPlayer.setTeam(team);
        teamPlayer.setPlayer(player);
        teamPlayer.setJoinedAt(LocalDateTime.now());
        teamPlayer.setStatus("ACTIVE");

        TeamPlayer saved =
                teamPlayerRepository.save(teamPlayer);

        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamPlayerResponseDTO getTeamPlayerById(
            String teamPlayerId) {

        TeamPlayer teamPlayer =
                teamPlayerRepository.findById(teamPlayerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "TeamPlayer membership not found"));

        return mapToResponseDTO(teamPlayer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamPlayerResponseDTO> getAllTeamPlayers() {

        return teamPlayerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamPlayerResponseDTO> getPlayersByTeam(
            String teamId) {

        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("Team not found");
        }

        return teamPlayerRepository
                .findByTeamTeamId(teamId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamPlayerResponseDTO> getTeamsByPlayer(
            String playerId) {

        if (!playerRepository.existsById(playerId)) {
            throw new IllegalArgumentException("Player not found");
        }

        return teamPlayerRepository
                .findByPlayerPlayerId(playerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public void removePlayerFromTeam(
            String teamPlayerId) {

        TeamPlayer teamPlayer =
                teamPlayerRepository.findById(teamPlayerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "TeamPlayer membership not found"));

        if ("INACTIVE".equalsIgnoreCase(
                teamPlayer.getStatus())) {

            throw new IllegalArgumentException(
                    "Player is already removed from this Team");
        }

        teamPlayer.setStatus("INACTIVE");

        teamPlayerRepository.save(teamPlayer);
    }

    private TeamPlayerResponseDTO mapToResponseDTO(
            TeamPlayer teamPlayer) {

        TeamPlayerResponseDTO response =
                new TeamPlayerResponseDTO();

        response.setTeamPlayerId(
                teamPlayer.getTeamPlayerId());

        response.setTeamId(
                teamPlayer.getTeam().getTeamId());

        response.setPlayerId(
                teamPlayer.getPlayer().getPlayerId());

        response.setJoinedAt(
                teamPlayer.getJoinedAt());

        response.setStatus(
                teamPlayer.getStatus());

        return response;
    }
}