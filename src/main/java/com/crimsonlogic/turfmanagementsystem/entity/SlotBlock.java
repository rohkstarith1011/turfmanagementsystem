package com.crimsonlogic.turfmanagementsystem.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
 
@Entity
@Table(name = "slot_block")
public class SlotBlock {
 
    @Id
    @Column(name = "slot_block_id")
    private String slotBlockId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playing_area_id", nullable = false)
    private PlayingArea playingArea;
 
    @Column(nullable = false)
    private LocalDate blockDate;
 
    @Column(nullable = false)
    private LocalTime startTime;
 
    @Column(nullable = false)
    private LocalTime endTime;
 
    @Column(nullable = false)
    private String reason;
 
    @Column(nullable = false)
    private String status;
 
    public SlotBlock() {
    }
 
    public SlotBlock(String slotBlockId, PlayingArea playingArea,
                     LocalDate blockDate, LocalTime startTime,
                     LocalTime endTime, String reason, String status) {
        this.slotBlockId = slotBlockId;
        this.playingArea = playingArea;
        this.blockDate = blockDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
    }
 
    public String getSlotBlockId() {
        return slotBlockId;
    }
 
    public void setSlotBlockId(String slotBlockId) {
        this.slotBlockId = slotBlockId;
    }
 
    public PlayingArea getPlayingArea() {
        return playingArea;
    }
 
    public void setPlayingArea(PlayingArea playingArea) {
        this.playingArea = playingArea;
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
    @PrePersist
    private void generateSlotBlockId() {
        if (slotBlockId == null || slotBlockId.isBlank()) {
            slotBlockId = EntityIdGenerator.generate("SLB");
        }
    }
}