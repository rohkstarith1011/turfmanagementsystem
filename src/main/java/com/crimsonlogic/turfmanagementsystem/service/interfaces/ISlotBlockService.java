package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotBlockRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SlotBlockResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ISlotBlockService {

    SlotBlockResponseDTO createSlotBlock(SlotBlockRequestDTO requestDTO);

    SlotBlockResponseDTO getSlotBlockById(String slotBlockId);

    List<SlotBlockResponseDTO> getAllSlotBlocks();

    List<SlotBlockResponseDTO> getSlotBlocksByPlayingArea(String playingAreaId);

    List<SlotBlockResponseDTO> getSlotBlocksByDate(LocalDate blockDate);

    List<SlotBlockResponseDTO> getSlotBlocksByPlayingAreaAndDate(
            String playingAreaId,
            LocalDate blockDate
    );

    SlotBlockResponseDTO updateSlotBlock(
            String slotBlockId,
            SlotBlockRequestDTO requestDTO
    );

    void deactivateSlotBlock(String slotBlockId);
}