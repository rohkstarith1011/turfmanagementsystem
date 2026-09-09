package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;

@Data
public class FacilityImageResponseDTO {
    private String imageId;
    private String facilityId;
    private String contentType;
    private Boolean isPrimary;
    private String imageUrl; // Constructed URL for frontend to use
}
