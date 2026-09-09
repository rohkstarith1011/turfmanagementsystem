package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;

@Data
public class AiDemandForecastResponseDTO {
    
    // LOW, MEDIUM, or HIGH
    private String demandLevel;
    
    // 0 to 100
    private Integer expectedOccupancyPercentage;
    
    // Detailed explanation
    private String explanation;
}
