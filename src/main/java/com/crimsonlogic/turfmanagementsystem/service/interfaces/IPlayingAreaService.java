package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.PlayingAreaRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.PlayingAreaResponseDTO;

public interface IPlayingAreaService {

    PlayingAreaResponseDTO createPlayingArea(
            PlayingAreaRequestDTO requestDTO);

    List<PlayingAreaResponseDTO> getAllPlayingAreas();

    PlayingAreaResponseDTO getPlayingAreaById(
            String playingAreaId);

    List<PlayingAreaResponseDTO> getPlayingAreasByFacilityId(
            String facilityId);

    List<PlayingAreaResponseDTO> getPlayingAreasByTurfSportId(
            String turfSportId);

    PlayingAreaResponseDTO updatePlayingArea(
            String playingAreaId,
            PlayingAreaRequestDTO requestDTO);

    void deactivatePlayingArea(
            String playingAreaId);
    
    public List<PlayingAreaResponseDTO> searchByName(String name);
}