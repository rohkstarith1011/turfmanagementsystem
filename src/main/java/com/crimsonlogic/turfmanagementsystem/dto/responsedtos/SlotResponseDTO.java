package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;


import java.time.LocalDate;
import java.time.LocalTime;

public class SlotResponseDTO {

    private String slotId;

    private String playingAreaId;

    private LocalDate slotDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status;

    // Getters and Setters

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getPlayingAreaId() {
        return playingAreaId;
    }

    public void setPlayingAreaId(String playingAreaId) {
        this.playingAreaId = playingAreaId;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
