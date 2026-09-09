package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;
import java.util.List;

@Data
public class RecommendedSlotDTO {
    private String slotId;
    private Integer matchScore; // 1-100
    private List<String> reasons;

    // Populated from DB post-AI validation
    private String playingAreaName;
    private String startTime;
    private String endTime;
}
