package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;
import java.util.List;

@Data
public class AiPricingSimulationResponseDTO {
    
    private Double basePrice;
    
    private Double recommendedPrice;
    
    private String demandLevel;
    
    private List<String> factors;
}
