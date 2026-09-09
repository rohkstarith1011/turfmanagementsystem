package com.crimsonlogic.turfmanagementsystem.controller;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityImageResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.FacilityImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityImageController {

    private final FacilityImageService facilityImageService;

    @Autowired
    public FacilityImageController(FacilityImageService facilityImageService) {
        this.facilityImageService = facilityImageService;
    }

    @PostMapping("/{facilityId}/images")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<FacilityImageResponseDTO> uploadImage(
            @PathVariable String facilityId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", required = false, defaultValue = "false") Boolean isPrimary) {
        FacilityImageResponseDTO dto = facilityImageService.uploadImage(facilityId, file, isPrimary);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{facilityId}/images")
    public ResponseEntity<List<FacilityImageResponseDTO>> getFacilityImages(@PathVariable String facilityId) {
        List<FacilityImageResponseDTO> dtos = facilityImageService.getFacilityImages(facilityId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageId) {
        Resource resource = facilityImageService.loadImageAsResource(imageId);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = java.nio.file.Files.probeContentType(resource.getFile().toPath());
        } catch (IOException ex) {
            System.err.println("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable String imageId) {
        facilityImageService.deleteImage(imageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/images/{imageId}/primary")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<FacilityImageResponseDTO> setPrimaryImage(@PathVariable String imageId) {
        FacilityImageResponseDTO dto = facilityImageService.setPrimaryImage(imageId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
