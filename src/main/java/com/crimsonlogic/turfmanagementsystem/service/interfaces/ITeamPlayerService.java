package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TeamPlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TeamPlayerResponseDTO;

public interface ITeamPlayerService {

    TeamPlayerResponseDTO addPlayerToTeam(
            TeamPlayerRequestDTO requestDTO);

    TeamPlayerResponseDTO getTeamPlayerById(
            String teamPlayerId);

    List<TeamPlayerResponseDTO> getAllTeamPlayers();

    List<TeamPlayerResponseDTO> getPlayersByTeam(
            String teamId);

    List<TeamPlayerResponseDTO> getTeamsByPlayer(
            String playerId);

    void removePlayerFromTeam(
            String teamPlayerId);
}