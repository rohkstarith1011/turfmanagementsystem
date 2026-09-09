package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;
import java.util.List;

@Data
public class AiTurfRecommendationResponseDTO {
    private List<RecommendedTurfDTO> recommendations;
}
