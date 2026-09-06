package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SlotResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.PlayingArea;
import com.crimsonlogic.turfmanagementsystem.entity.Slot;
import com.crimsonlogic.turfmanagementsystem.entity.SlotBlock;
import com.crimsonlogic.turfmanagementsystem.repository.PlayingAreaRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotBlockRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISlotService;

@Service
@Transactional
public class SlotServiceImpl implements ISlotService {

    private final SlotRepository slotRepository;
    private final PlayingAreaRepository playingAreaRepository;
    private final SlotBlockRepository slotBlockRepository;

    public SlotServiceImpl(
            SlotRepository slotRepository,
            PlayingAreaRepository playingAreaRepository,
            SlotBlockRepository slotBlockRepository) {

        this.slotRepository = slotRepository;
        this.playingAreaRepository = playingAreaRepository;
        this.slotBlockRepository = slotBlockRepository;
    }

    @Override
    public SlotResponseDTO createSlot(SlotRequestDTO requestDTO) {

        PlayingArea playingArea = getActivePlayingArea(
                requestDTO.getPlayingAreaId());

        validateSlotDate(requestDTO.getSlotDate());
        validateTime(requestDTO.getStartTime(), requestDTO.getEndTime());

        Facility facility = getActiveFacility(playingArea);

        validateOperatingHours(
                requestDTO.getStartTime(),
                requestDTO.getEndTime(),
                facility);

        validateSlotOverlap(
                requestDTO.getPlayingAreaId(),
                requestDTO.getSlotDate(),
                requestDTO.getStartTime(),
                requestDTO.getEndTime(),
                null);

        validateSlotBlockOverlap(
                requestDTO.getPlayingAreaId(),
                requestDTO.getSlotDate(),
                requestDTO.getStartTime(),
                requestDTO.getEndTime());

        Slot slot = new Slot();

        slot.setPlayingArea(playingArea);
        slot.setSlotDate(requestDTO.getSlotDate());
        slot.setStartTime(requestDTO.getStartTime());
        slot.setEndTime(requestDTO.getEndTime());
        slot.setStatus("ACTIVE");

        return mapToResponseDTO(slotRepository.save(slot));
    }

    @Override
    @Transactional(readOnly = true)
    public SlotResponseDTO getSlotById(String slotId) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Slot not found"));

        return mapToResponseDTO(slot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponseDTO> getAllSlots() {

        return slotRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponseDTO> getSlotsByPlayingArea(
            String playingAreaId) {

        return slotRepository
                .findByPlayingAreaPlayingAreaId(playingAreaId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponseDTO> getSlotsByDate(LocalDate slotDate) {

        return slotRepository.findBySlotDate(slotDate)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponseDTO> getSlotsByPlayingAreaAndDate(
            String playingAreaId,
            LocalDate slotDate) {

        return slotRepository
                .findByPlayingAreaPlayingAreaIdAndSlotDate(
                        playingAreaId,
                        slotDate)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SlotResponseDTO updateSlot(
            String slotId,
            SlotRequestDTO requestDTO) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Slot not found"));

        PlayingArea playingArea = playingAreaRepository
                .findById(requestDTO.getPlayingAreaId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Playing area not found"));

        if (!slot.getPlayingArea()
                .getPlayingAreaId()
                .equals(playingArea.getPlayingAreaId())) {

            throw new IllegalArgumentException(
                    "Playing area cannot be changed");
        }

        validateActivePlayingArea(playingArea);

        validateSlotDate(requestDTO.getSlotDate());
        validateTime(requestDTO.getStartTime(), requestDTO.getEndTime());

        Facility facility = getActiveFacility(playingArea);

        validateOperatingHours(
                requestDTO.getStartTime(),
                requestDTO.getEndTime(),
                facility);

        validateSlotOverlap(
                playingArea.getPlayingAreaId(),
                requestDTO.getSlotDate(),
                requestDTO.getStartTime(),
                requestDTO.getEndTime(),
                slotId);

        validateSlotBlockOverlap(
                playingArea.getPlayingAreaId(),
                requestDTO.getSlotDate(),
                requestDTO.getStartTime(),
                requestDTO.getEndTime());

        slot.setSlotDate(requestDTO.getSlotDate());
        slot.setStartTime(requestDTO.getStartTime());
        slot.setEndTime(requestDTO.getEndTime());

        if ("INACTIVE".equalsIgnoreCase(slot.getStatus())) {
            slot.setStatus("ACTIVE");
        }

        return mapToResponseDTO(slotRepository.save(slot));
    }

    @Override
    public void deactivateSlot(String slotId) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Slot not found"));

        if ("INACTIVE".equalsIgnoreCase(slot.getStatus())) {
            throw new IllegalArgumentException(
                    "Slot is already inactive");
        }

        slot.setStatus("INACTIVE");

        slotRepository.save(slot);
    }

    private PlayingArea getActivePlayingArea(String playingAreaId) {

        PlayingArea playingArea = playingAreaRepository
                .findById(playingAreaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Playing area not found"));

        validateActivePlayingArea(playingArea);

        return playingArea;
    }

    private void validateActivePlayingArea(PlayingArea playingArea) {

        if (!"ACTIVE".equalsIgnoreCase(playingArea.getStatus())) {
            throw new IllegalArgumentException(
                    "Playing area is inactive");
        }
    }

    private Facility getActiveFacility(PlayingArea playingArea) {

        Facility facility = playingArea.getFacility();

        if (facility == null) {
            throw new IllegalArgumentException(
                    "Facility not found for playing area");
        }

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Facility is inactive");
        }

        return facility;
    }

    private void validateSlotDate(LocalDate slotDate) {

        if (slotDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Slot date cannot be in the past");
        }
    }

    private void validateTime(
            LocalTime startTime,
            LocalTime endTime) {

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }
    }

    private void validateOperatingHours(
            LocalTime startTime,
            LocalTime endTime,
            Facility facility) {

        if (startTime.isBefore(facility.getOpeningTime())
                || endTime.isAfter(facility.getClosingTime())) {

            throw new IllegalArgumentException(
                    "Slot time must be within facility operating hours");
        }
    }

    private void validateSlotOverlap(
            String playingAreaId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime,
            String currentSlotId) {

        List<Slot> overlappingSlots =
                slotRepository
                        .findByPlayingAreaPlayingAreaIdAndSlotDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                playingAreaId,
                                slotDate,
                                endTime,
                                startTime);

        boolean overlapExists = overlappingSlots
                .stream()
                .anyMatch(existingSlot ->
                        "ACTIVE".equalsIgnoreCase(existingSlot.getStatus())
                        && (currentSlotId == null
                        || !existingSlot.getSlotId()
                                .equals(currentSlotId)));

        if (overlapExists) {
            throw new IllegalArgumentException(
                    "Slot overlaps with an existing active slot");
        }
    }

    private void validateSlotBlockOverlap(
            String playingAreaId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime) {

        List<SlotBlock> overlappingBlocks =
                slotBlockRepository
                        .findByPlayingAreaPlayingAreaIdAndBlockDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                playingAreaId,
                                slotDate,
                                endTime,
                                startTime);

        boolean activeBlockExists = overlappingBlocks
                .stream()
                .anyMatch(block ->
                        "ACTIVE".equalsIgnoreCase(block.getStatus()));

        if (activeBlockExists) {
            throw new IllegalArgumentException(
                    "Slot overlaps with an existing active slot block");
        }
    }

    private SlotResponseDTO mapToResponseDTO(Slot slot) {

        SlotResponseDTO responseDTO = new SlotResponseDTO();

        responseDTO.setSlotId(slot.getSlotId());
        responseDTO.setPlayingAreaId(
                slot.getPlayingArea().getPlayingAreaId());
        responseDTO.setSlotDate(slot.getSlotDate());
        responseDTO.setStartTime(slot.getStartTime());
        responseDTO.setEndTime(slot.getEndTime());
        responseDTO.setStatus(slot.getStatus());

        return responseDTO;
    }
}