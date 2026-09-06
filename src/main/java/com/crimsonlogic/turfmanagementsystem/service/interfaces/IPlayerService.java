package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PlayerRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PlayerResponseDTO;

public interface IPlayerService {

    PlayerResponseDTO createPlayer(PlayerRequestDTO requestDTO);

    PlayerResponseDTO getPlayerById(String playerId);

    List<PlayerResponseDTO> getAllPlayers();

    PlayerResponseDTO updatePlayer(
            String playerId,
            PlayerRequestDTO requestDTO);

    void deactivatePlayer(String playerId);
    List<PlayerResponseDTO> getPlayersByName(String name);
}