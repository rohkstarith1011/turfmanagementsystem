package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.SlotBlockRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.SlotBlockResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ISlotBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slot-blocks")
public class SlotBlockController {

    private final ISlotBlockService slotBlockService;

    public SlotBlockController(ISlotBlockService slotBlockService) {
        this.slotBlockService = slotBlockService;
    }

    @PostMapping
    public ResponseEntity<SlotBlockResponseDTO> createSlotBlock(
            @RequestBody SlotBlockRequestDTO requestDTO) {

        return ResponseEntity.status(201)
                .body(slotBlockService.createSlotBlock(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<SlotBlockResponseDTO>> getAllSlotBlocks() {

        return ResponseEntity.ok(
                slotBlockService.getAllSlotBlocks());
    }

    @GetMapping("/{slotBlockId}")
    public ResponseEntity<SlotBlockResponseDTO> getSlotBlockById(
            @PathVariable String slotBlockId) {

        return ResponseEntity.ok(
                slotBlockService.getSlotBlockById(slotBlockId));
    }

    @GetMapping("/playing-area/{playingAreaId}")
    public ResponseEntity<List<SlotBlockResponseDTO>>
    getSlotBlocksByPlayingArea(
            @PathVariable String playingAreaId) {

        return ResponseEntity.ok(
                slotBlockService.getSlotBlocksByPlayingArea(
                        playingAreaId));
    }

    @GetMapping("/date/{blockDate}")
    public ResponseEntity<List<SlotBlockResponseDTO>>
    getSlotBlocksByDate(
            @PathVariable LocalDate blockDate) {

        return ResponseEntity.ok(
                slotBlockService.getSlotBlocksByDate(blockDate));
    }

    @GetMapping("/playing-area/{playingAreaId}/date/{blockDate}")
    public ResponseEntity<List<SlotBlockResponseDTO>>
    getSlotBlocksByPlayingAreaAndDate(
            @PathVariable String playingAreaId,
            @PathVariable LocalDate blockDate) {

        return ResponseEntity.ok(
                slotBlockService
                        .getSlotBlocksByPlayingAreaAndDate(
                                playingAreaId,
                                blockDate));
    }

    @PutMapping("/{slotBlockId}")
    public ResponseEntity<SlotBlockResponseDTO> updateSlotBlock(
            @PathVariable String slotBlockId,
            @RequestBody SlotBlockRequestDTO requestDTO) {

        return ResponseEntity.ok(
                slotBlockService.updateSlotBlock(
                        slotBlockId,
                        requestDTO));
    }

    @PatchMapping("/{slotBlockId}/deactivate")
    public ResponseEntity<Void> deactivateSlotBlock(
            @PathVariable String slotBlockId) {

        slotBlockService.deactivateSlotBlock(slotBlockId);

        return ResponseEntity.noContent().build();
    }
}