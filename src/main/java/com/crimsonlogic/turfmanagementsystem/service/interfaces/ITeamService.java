package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.TeamRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TeamResponseDTO;

public interface ITeamService {

    TeamResponseDTO createTeam(TeamRequestDTO requestDTO);

    TeamResponseDTO getTeamById(String teamId);

    List<TeamResponseDTO> getAllTeams();

    List<TeamResponseDTO> getTeamsByPlayer(String playerId);

    List<TeamResponseDTO> getTeamsBySport(String sportId);

    List<TeamResponseDTO> getTeamsByName(String name);

    TeamResponseDTO updateTeam(
            String teamId,
            TeamRequestDTO requestDTO);

    void deactivateTeam(String teamId);
}