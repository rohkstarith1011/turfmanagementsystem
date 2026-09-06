package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AmenityRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AmenityResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Amenity;
import com.crimsonlogic.turfmanagementsystem.repository.AmenityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmenityServiceImplTest {

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private AmenityServiceImpl amenityService;

    private Amenity amenity;
    private AmenityRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        amenity = new Amenity(
                "AMN001",
                "Parking",
                "Parking facility available",
                "ACTIVE"
        );

        requestDTO = new AmenityRequestDTO();
        requestDTO.setName("Parking");
        requestDTO.setDescription("Parking facility available");
    }

    @Test
    void createAmenitySuccessfully() {

        when(amenityRepository.existsByNameIgnoreCase("Parking"))
                .thenReturn(false);
        when(amenityRepository.save(any(Amenity.class)))
                .thenReturn(amenity);

        AmenityResponseDTO result =
                amenityService.createAmenity(requestDTO);

        assertNotNull(result);
        assertEquals("AMN001", result.getAmenityId());
        assertEquals("Parking", result.getName());
        assertEquals("Parking facility available", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());

        verify(amenityRepository)
                .existsByNameIgnoreCase("Parking");
        verify(amenityRepository)
                .save(any(Amenity.class));
    }

    @Test
    void createAmenity_DuplicateNameRejected() {

        when(amenityRepository.existsByNameIgnoreCase("Parking"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> amenityService.createAmenity(requestDTO)
        );

        assertEquals(
                "Amenity with this name already exists",
                exception.getMessage()
        );

        verify(amenityRepository)
                .existsByNameIgnoreCase("Parking");
        verify(amenityRepository, never())
                .save(any(Amenity.class));
    }

    @Test
    void getAllAmenitiesSuccessfully() {

        Amenity parking = new Amenity(
                "AMN001",
                "Parking",
                "Parking facility available",
                "ACTIVE"
        );

        Amenity washroom = new Amenity(
                "AMN002",
                "Washroom",
                "Washroom facility available",
                "ACTIVE"
        );

        Amenity inactiveAmenity = new Amenity(
                "AMN003",
                "Changing Room",
                "Changing room facility available",
                "INACTIVE"
        );

        when(amenityRepository.findAll())
                .thenReturn(List.of(parking, washroom, inactiveAmenity));

        List<AmenityResponseDTO> result =
                amenityService.getAllAmenities();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals("Parking", result.get(0).getName());
        assertEquals("Washroom", result.get(1).getName());
        assertEquals("Changing Room", result.get(2).getName());

        assertEquals("ACTIVE", result.get(0).getStatus());
        assertEquals("ACTIVE", result.get(1).getStatus());
        assertEquals("INACTIVE", result.get(2).getStatus());

        verify(amenityRepository).findAll();
    }

    @Test
    void getAllAmenities_WhenNoAmenitiesExist_ReturnsEmptyList() {

        when(amenityRepository.findAll())
                .thenReturn(List.of());

        List<AmenityResponseDTO> result =
                amenityService.getAllAmenities();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(amenityRepository).findAll();
    }

    @Test
    void getAmenityByIdSuccessfully() {

        when(amenityRepository.findById("AMN001"))
                .thenReturn(Optional.of(amenity));

        AmenityResponseDTO result =
                amenityService.getAmenityById("AMN001");

        assertNotNull(result);
        assertEquals("AMN001", result.getAmenityId());
        assertEquals("Parking", result.getName());
        assertEquals(
                "Parking facility available",
                result.getDescription()
        );
        assertEquals("ACTIVE", result.getStatus());

        verify(amenityRepository).findById("AMN001");
    }

    @Test
    void getAmenityById_AmenityNotFound() {

        when(amenityRepository.findById("AMN999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> amenityService.getAmenityById("AMN999")
        );

        assertEquals("Amenity not found", exception.getMessage());

        verify(amenityRepository).findById("AMN999");
    }

    @Test
    void getAmenityByNameSuccessfully() {

        when(amenityRepository.findByNameIgnoreCase("parking"))
                .thenReturn(Optional.of(amenity));

        AmenityResponseDTO result =
                amenityService.getAmenityByName("parking");

        assertNotNull(result);
        assertEquals("AMN001", result.getAmenityId());
        assertEquals("Parking", result.getName());
        assertEquals(
                "Parking facility available",
                result.getDescription()
        );
        assertEquals("ACTIVE", result.getStatus());

        verify(amenityRepository)
                .findByNameIgnoreCase("parking");
    }

    @Test
    void getAmenityByName_AmenityNotFound() {

        when(amenityRepository.findByNameIgnoreCase("Gym"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> amenityService.getAmenityByName("Gym")
        );

        assertEquals("Amenity not found", exception.getMessage());

        verify(amenityRepository)
                .findByNameIgnoreCase("Gym");
    }

    @Test
    void updateAmenitySuccessfully() {

        AmenityRequestDTO updateRequest = new AmenityRequestDTO();
        updateRequest.setName("Premium Parking");
        updateRequest.setDescription(
                "Premium parking facility available"
        );

        when(amenityRepository.findById("AMN001"))
                .thenReturn(Optional.of(amenity));

        when(amenityRepository.existsByNameIgnoreCase("Premium Parking"))
                .thenReturn(false);

        when(amenityRepository.save(amenity))
                .thenReturn(amenity);

        AmenityResponseDTO result =
                amenityService.updateAmenity(
                        "AMN001",
                        updateRequest
                );

        assertNotNull(result);
        assertEquals("AMN001", result.getAmenityId());
        assertEquals("Premium Parking", amenity.getName());
        assertEquals(
                "Premium parking facility available",
                amenity.getDescription()
        );

        assertEquals(
                "Premium Parking",
                result.getName()
        );

        assertEquals(
                "Premium parking facility available",
                result.getDescription()
        );

        assertEquals("ACTIVE", result.getStatus());

        verify(amenityRepository).findById("AMN001");
        verify(amenityRepository)
                .existsByNameIgnoreCase("Premium Parking");
        verify(amenityRepository).save(amenity);
    }

    @Test
    void updateAmenity_AmenityNotFound() {

        AmenityRequestDTO updateRequest = new AmenityRequestDTO();
        updateRequest.setName("Parking");
        updateRequest.setDescription("Updated parking");

        when(amenityRepository.findById("AMN999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> amenityService.updateAmenity(
                        "AMN999",
                        updateRequest
                )
        );

        assertEquals("Amenity not found", exception.getMessage());

        verify(amenityRepository).findById("AMN999");
        verify(amenityRepository, never())
                .save(any(Amenity.class));
    }

    @Test
    void updateAmenity_DuplicateNameRejected() {

        AmenityRequestDTO updateRequest = new AmenityRequestDTO();
        updateRequest.setName("Washroom");
        updateRequest.setDescription("Updated washroom");

        when(amenityRepository.findById("AMN001"))
                .thenReturn(Optional.of(amenity));

        when(amenityRepository.existsByNameIgnoreCase("Washroom"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> amenityService.updateAmenity(
                        "AMN001",
                        updateRequest
                )
        );

        assertEquals(
                "Amenity with this name already exists",
                exception.getMessage()
        );

        verify(amenityRepository).findById("AMN001");
        verify(amenityRepository)
                .existsByNameIgnoreCase("Washroom");
        verify(amenityRepository, never())
                .save(any(Amenity.class));
    }

    @Test
    void updateAmenity_SameNameAllowed() {

        AmenityRequestDTO updateRequest = new AmenityRequestDTO();
        updateRequest.setName("Parking");
        updateRequest.setDescription(
                "Updated parking description"
        );

        when(amenityRepository.findById("AMN001"))
                .thenReturn(Optional.of(amenity));

        when(amenityRepository.save(amenity))
                .thenReturn(amenity);

        AmenityResponseDTO result =
                amenityService.updateAmenity(
                        "AMN001",
                        updateRequest
                );

        assertNotNull(result);
        assertEquals("Parking", result.getName());
        assertEquals(
                "Updated parking description",
                result.getDescription()
        );

        verify(amenityRepository).findById("AMN001");
        verify(amenityRepository, never())
                .existsByNameIgnoreCase("Parking");
        verify(amenityRepository).save(amenity);
    }

    @Test
    void deactivateAmenitySuccessfully() {

        when(amenityRepository.findById("AMN001"))
                .thenReturn(Optional.of(amenity));

        when(amenityRepository.save(amenity))
                .thenReturn(amenity);

        amenityService.deactivateAmenity("AMN001");

        assertEquals("INACTIVE", amenity.getStatus());

        verify(amenityRepository).findById("AMN001");
        verify(amenityRepository).save(amenity);
    }

    @Test
    void deactivateAmenity_AmenityNotFound() {

        when(amenityRepository.findById("AMN999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> amenityService.deactivateAmenity("AMN999")
        );

        assertEquals("Amenity not found", exception.getMessage());

        verify(amenityRepository).findById("AMN999");
        verify(amenityRepository, never())
                .save(any(Amenity.class));
    }
}