package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;
import java.util.List;

@Data
public class RecommendedTurfDTO {
    private String facilityId;
    private String facilityName;
    private Integer matchScore; // 1-100
    private List<String> reasons;
    
    // These will be overridden by the backend post-validation
    private Double basePrice;
    private Double rating;
}
