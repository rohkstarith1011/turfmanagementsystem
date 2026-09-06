package com.crimsonlogic.turfmanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SlotResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final ISlotService slotService;

    public SlotController(ISlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    public ResponseEntity<SlotResponseDTO> createSlot(
            @Valid @RequestBody SlotRequestDTO requestDTO) {

        return new ResponseEntity<>(
                slotService.createSlot(requestDTO),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SlotResponseDTO>> getAllSlots() {

        return ResponseEntity.ok(
                slotService.getAllSlots());
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<SlotResponseDTO> getSlotById(
            @PathVariable String slotId) {

        return ResponseEntity.ok(
                slotService.getSlotById(slotId));
    }

    @GetMapping("/playing-area/{playingAreaId}")
    public ResponseEntity<List<SlotResponseDTO>> getSlotsByPlayingArea(
            @PathVariable String playingAreaId) {

        return ResponseEntity.ok(
                slotService.getSlotsByPlayingArea(playingAreaId));
    }

    @GetMapping("/date/{slotDate}")
    public ResponseEntity<List<SlotResponseDTO>> getSlotsByDate(
            @PathVariable LocalDate slotDate) {

        return ResponseEntity.ok(
                slotService.getSlotsByDate(slotDate));
    }

    @GetMapping("/playing-area/{playingAreaId}/date/{slotDate}")
    public ResponseEntity<List<SlotResponseDTO>> getSlotsByPlayingAreaAndDate(
            @PathVariable String playingAreaId,
            @PathVariable LocalDate slotDate) {

        return ResponseEntity.ok(
                slotService.getSlotsByPlayingAreaAndDate(
                        playingAreaId,
                        slotDate));
    }

    @PutMapping("/{slotId}")
    public ResponseEntity<SlotResponseDTO> updateSlot(
            @PathVariable String slotId,
            @Valid @RequestBody SlotRequestDTO requestDTO) {

        return ResponseEntity.ok(
                slotService.updateSlot(
                        slotId,
                        requestDTO));
    }

    @PatchMapping("/{slotId}/deactivate")
    public ResponseEntity<Void> deactivateSlot(
            @PathVariable String slotId) {

        slotService.deactivateSlot(slotId);

        return ResponseEntity.noContent().build();
    }
}