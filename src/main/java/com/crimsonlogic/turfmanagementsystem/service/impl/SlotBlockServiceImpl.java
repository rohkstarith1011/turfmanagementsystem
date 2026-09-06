package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotBlockRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SlotBlockResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.PlayingArea;
import com.crimsonlogic.turfmanagementsystem.entity.SlotBlock;
import com.crimsonlogic.turfmanagementsystem.repository.PlayingAreaRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotBlockRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISlotBlockService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SlotBlockServiceImpl implements ISlotBlockService {

    private final SlotBlockRepository slotBlockRepository;
    private final PlayingAreaRepository playingAreaRepository;

    public SlotBlockServiceImpl(
            SlotBlockRepository slotBlockRepository,
            PlayingAreaRepository playingAreaRepository) {

        this.slotBlockRepository = slotBlockRepository;
        this.playingAreaRepository = playingAreaRepository;
    }

    @Override
    public SlotBlockResponseDTO createSlotBlock(
            SlotBlockRequestDTO requestDTO) {

        PlayingArea playingArea = playingAreaRepository
                .findById(requestDTO.getPlayingAreaId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Playing area not found"));

        if (!"ACTIVE".equalsIgnoreCase(playingArea.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot create slot block for an inactive playing area");
        }

        if (!requestDTO.getStartTime()
                .isBefore(requestDTO.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        if (requestDTO.getBlockDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Block date cannot be in the past");
        }

        boolean overlapping = slotBlockRepository
                .findByPlayingAreaPlayingAreaIdAndBlockDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        requestDTO.getPlayingAreaId(),
                        requestDTO.getBlockDate(),
                        requestDTO.getEndTime(),
                        requestDTO.getStartTime()
                )
                .stream()
                .anyMatch(existingBlock ->
                        "ACTIVE".equalsIgnoreCase(
                                existingBlock.getStatus()));

        if (overlapping) {
            throw new IllegalArgumentException(
                    "Slot block overlaps with an existing active slot block");
        }

        SlotBlock slotBlock = new SlotBlock();

        slotBlock.setPlayingArea(playingArea);
        slotBlock.setBlockDate(requestDTO.getBlockDate());
        slotBlock.setStartTime(requestDTO.getStartTime());
        slotBlock.setEndTime(requestDTO.getEndTime());
        slotBlock.setReason(requestDTO.getReason());
        slotBlock.setStatus("ACTIVE");

        SlotBlock savedSlotBlock =
                slotBlockRepository.save(slotBlock);

        return mapToResponseDTO(savedSlotBlock);
    }

    @Override
    public SlotBlockResponseDTO getSlotBlockById(
            String slotBlockId) {

        SlotBlock slotBlock = slotBlockRepository
                .findById(slotBlockId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Slot block not found"));

        return mapToResponseDTO(slotBlock);
    }

    @Override
    public List<SlotBlockResponseDTO> getAllSlotBlocks() {

        return slotBlockRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<SlotBlockResponseDTO> getSlotBlocksByPlayingArea(
            String playingAreaId) {

        return slotBlockRepository
                .findByPlayingAreaPlayingAreaId(playingAreaId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<SlotBlockResponseDTO> getSlotBlocksByDate(
            LocalDate blockDate) {

        return slotBlockRepository
                .findByBlockDate(blockDate)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<SlotBlockResponseDTO>
    getSlotBlocksByPlayingAreaAndDate(
            String playingAreaId,
            LocalDate blockDate) {

        return slotBlockRepository
                .findByPlayingAreaPlayingAreaIdAndBlockDate(
                        playingAreaId,
                        blockDate)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public SlotBlockResponseDTO updateSlotBlock(
            String slotBlockId,
            SlotBlockRequestDTO requestDTO) {

        SlotBlock slotBlock = slotBlockRepository
                .findById(slotBlockId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Slot block not found"));

        PlayingArea playingArea = playingAreaRepository
                .findById(requestDTO.getPlayingAreaId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Playing area not found"));

        /*
         * Playing area cannot be changed during update.
         */
        if (!slotBlock.getPlayingArea()
                .getPlayingAreaId()
                .equals(requestDTO.getPlayingAreaId())) {

            throw new IllegalArgumentException(
                    "Playing area cannot be changed");
        }

        if (!"ACTIVE".equalsIgnoreCase(
                playingArea.getStatus())) {

            throw new IllegalArgumentException(
                    "Cannot update slot block for an inactive playing area");
        }

        if (!requestDTO.getStartTime()
                .isBefore(requestDTO.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        if (requestDTO.getBlockDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Block date cannot be in the past");
        }

        /*
         * Check for overlapping ACTIVE blocks.
         *
         * The repository query also returns the current block,
         * so we explicitly exclude it using its ID.
         */
        boolean overlapping = slotBlockRepository
                .findByPlayingAreaPlayingAreaIdAndBlockDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        requestDTO.getPlayingAreaId(),
                        requestDTO.getBlockDate(),
                        requestDTO.getEndTime(),
                        requestDTO.getStartTime()
                )
                .stream()
                .anyMatch(existingBlock ->
                        !existingBlock.getSlotBlockId()
                                .equals(slotBlockId)
                                && "ACTIVE".equalsIgnoreCase(
                                existingBlock.getStatus()));

        if (overlapping) {
            throw new IllegalArgumentException(
                    "Slot block overlaps with an existing active slot block");
        }

        slotBlock.setBlockDate(requestDTO.getBlockDate());
        slotBlock.setStartTime(requestDTO.getStartTime());
        slotBlock.setEndTime(requestDTO.getEndTime());
        slotBlock.setReason(requestDTO.getReason());

        /*
         * PUT also reactivates an inactive SlotBlock.
         */
        if ("INACTIVE".equalsIgnoreCase(
                slotBlock.getStatus())) {

            slotBlock.setStatus("ACTIVE");
        }

        SlotBlock updatedSlotBlock =
                slotBlockRepository.save(slotBlock);

        return mapToResponseDTO(updatedSlotBlock);
    }

    @Override
    public void deactivateSlotBlock(String slotBlockId) {

        SlotBlock slotBlock = slotBlockRepository
                .findById(slotBlockId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Slot block not found"));

        if ("INACTIVE".equalsIgnoreCase(
                slotBlock.getStatus())) {

            throw new IllegalArgumentException(
                    "Slot block is already inactive");
        }

        slotBlock.setStatus("INACTIVE");

        slotBlockRepository.save(slotBlock);
    }

    private SlotBlockResponseDTO mapToResponseDTO(
            SlotBlock slotBlock) {

        SlotBlockResponseDTO responseDTO =
                new SlotBlockResponseDTO();

        responseDTO.setSlotBlockId(
                slotBlock.getSlotBlockId());

        responseDTO.setPlayingAreaId(
                slotBlock.getPlayingArea()
                        .getPlayingAreaId());

        responseDTO.setBlockDate(
                slotBlock.getBlockDate());

        responseDTO.setStartTime(
                slotBlock.getStartTime());

        responseDTO.setEndTime(
                slotBlock.getEndTime());

        responseDTO.setReason(
                slotBlock.getReason());

        responseDTO.setStatus(
                slotBlock.getStatus());

        return responseDTO;
    }
}