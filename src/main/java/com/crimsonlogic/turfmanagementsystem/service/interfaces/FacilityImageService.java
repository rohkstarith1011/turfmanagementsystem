package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityImageResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FacilityImageService {
    FacilityImageResponseDTO uploadImage(String facilityId, MultipartFile file, Boolean isPrimary);
    List<FacilityImageResponseDTO> getFacilityImages(String facilityId);
    Resource loadImageAsResource(String imageId);
    void deleteImage(String imageId);
    FacilityImageResponseDTO setPrimaryImage(String imageId);
}
