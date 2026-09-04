package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



import java.time.LocalDate;
import java.time.LocalTime;

public class SlotBlockResponseDTO {

    private String slotBlockId;

    private String playingAreaId;

    private LocalDate blockDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String reason;

    private String status;

    // Getters and Setters

    public String getSlotBlockId() {
        return slotBlockId;
    }

    public void setSlotBlockId(String slotBlockId) {
        this.slotBlockId = slotBlockId;
    }

    public String getPlayingAreaId() {
        return playingAreaId;
    }

    public void setPlayingAreaId(String playingAreaId) {
        this.playingAreaId = playingAreaId;
    }

    public LocalDate getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(LocalDate blockDate) {
        this.blockDate = blockDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}