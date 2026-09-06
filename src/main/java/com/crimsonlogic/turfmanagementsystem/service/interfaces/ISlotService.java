package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SlotResponseDTO;

public interface ISlotService {

    SlotResponseDTO createSlot(SlotRequestDTO requestDTO);

    SlotResponseDTO getSlotById(String slotId);

    List<SlotResponseDTO> getAllSlots();

    List<SlotResponseDTO> getSlotsByPlayingArea(String playingAreaId);

    List<SlotResponseDTO> getSlotsByDate(LocalDate slotDate);

    List<SlotResponseDTO> getSlotsByPlayingAreaAndDate(
            String playingAreaId,
            LocalDate slotDate);

    SlotResponseDTO updateSlot(
            String slotId,
            SlotRequestDTO requestDTO);

    void deactivateSlot(String slotId);
}