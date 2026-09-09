package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityImageResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.FacilityImage;
import com.crimsonlogic.turfmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityImageRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.FacilityImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacilityImageServiceImpl implements FacilityImageService {

    private final Path fileStorageLocation;
    private final FacilityImageRepository facilityImageRepository;
    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityImageServiceImpl(FacilityImageRepository facilityImageRepository, FacilityRepository facilityRepository) {
        this.facilityImageRepository = facilityImageRepository;
        this.facilityRepository = facilityRepository;
        
        this.fileStorageLocation = Paths.get("uploads/facilities").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public FacilityImageResponseDTO uploadImage(String facilityId, MultipartFile file, Boolean isPrimary) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + facilityId));

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FacilityImage image = new FacilityImage();
            image.setFacility(facility);
            image.setImagePath(fileName);
            image.setContentType(file.getContentType());
            image.setIsPrimary(isPrimary != null ? isPrimary : false);

            if(image.getIsPrimary()) {
                // unset other primary images for this facility
                List<FacilityImage> existingImages = facilityImageRepository.findByFacility_FacilityId(facilityId);
                for(FacilityImage img : existingImages) {
                    if(img.getIsPrimary()) {
                        img.setIsPrimary(false);
                        facilityImageRepository.save(img);
                    }
                }
            }

            FacilityImage savedImage = facilityImageRepository.save(image);
            return mapToDTO(savedImage);
            
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public List<FacilityImageResponseDTO> getFacilityImages(String facilityId) {
        return facilityImageRepository.findByFacility_FacilityId(facilityId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Resource loadImageAsResource(String imageId) {
        FacilityImage image = facilityImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        try {
            Path filePath = this.fileStorageLocation.resolve(image.getImagePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found " + image.getImagePath());
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found " + image.getImagePath() + " due to " + ex.getMessage());
        }
    }

    @Override
    public void deleteImage(String imageId) {
        FacilityImage image = facilityImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        try {
            Path filePath = this.fileStorageLocation.resolve(image.getImagePath()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Log warning but continue with DB deletion
            System.err.println("Could not delete file: " + image.getImagePath());
        }

        facilityImageRepository.delete(image);
    }

    @Override
    public FacilityImageResponseDTO setPrimaryImage(String imageId) {
        FacilityImage image = facilityImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        List<FacilityImage> existingImages = facilityImageRepository.findByFacility_FacilityId(image.getFacility().getFacilityId());
        for(FacilityImage img : existingImages) {
            if(img.getIsPrimary()) {
                img.setIsPrimary(false);
                facilityImageRepository.save(img);
            }
        }

        image.setIsPrimary(true);
        FacilityImage savedImage = facilityImageRepository.save(image);
        return mapToDTO(savedImage);
    }

    private FacilityImageResponseDTO mapToDTO(FacilityImage image) {
        FacilityImageResponseDTO dto = new FacilityImageResponseDTO();
        dto.setImageId(image.getImageId());
        dto.setFacilityId(image.getFacility().getFacilityId());
        dto.setContentType(image.getContentType());
        dto.setIsPrimary(image.getIsPrimary());
        
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/facilities/images/")
                .path(image.getImageId())
                .toUriString();
        dto.setImageUrl(imageUrl);
        return dto;
    }
}
