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
@Table(name = "slot")
public class Slot {
 
    @Id
    @Column(name = "slot_id")
    private String slotId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playing_area_id", nullable = false)
    private PlayingArea playingArea;
 
    @Column(nullable = false)
    private LocalDate slotDate;
 
    @Column(nullable = false)
    private LocalTime startTime;
 
    @Column(nullable = false)
    private LocalTime endTime;
 
    @Column(nullable = false)
    private String status;
 
    public Slot() {
    }
 
    public Slot(String slotId, PlayingArea playingArea,
                LocalDate slotDate, LocalTime startTime,
                LocalTime endTime, String status) {
        this.slotId = slotId;
        this.playingArea = playingArea;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
 
    public String getSlotId() {
        return slotId;
    }
 
    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
 
    public PlayingArea getPlayingArea() {
        return playingArea;
    }
 
    public void setPlayingArea(PlayingArea playingArea) {
        this.playingArea = playingArea;
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
    @PrePersist
    private void generateSlotId() {
        if (slotId == null || slotId.isBlank()) {
            slotId = EntityIdGenerator.generate("SLT");
        }
    }
}
