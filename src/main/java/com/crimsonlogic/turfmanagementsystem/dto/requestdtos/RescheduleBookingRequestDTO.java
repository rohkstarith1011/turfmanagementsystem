package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;

import jakarta.validation.constraints.NotBlank;

public class RescheduleBookingRequestDTO {

    @NotBlank(message = "New slot ID is required")
    private String newSlotId;

    public String getNewSlotId() {
        return newSlotId;
    }

    public void setNewSlotId(String newSlotId) {
        this.newSlotId = newSlotId;
    }
}