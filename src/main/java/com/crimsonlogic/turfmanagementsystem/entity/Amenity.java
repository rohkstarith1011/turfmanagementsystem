package com.crimsonlogic.turfmanagementsystem.entity;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
 
@Entity
@Table(name = "amenity")
public class Amenity {
 
    @Id
    @Column(name = "amenity_id")
    private String amenityId;
 
    @Column(nullable = false, unique = true)
    private String name;
 
    @Column(nullable = false)
    private String description;
 
    @Column(nullable = false)
    private String status;
 
    public Amenity() {
    }
 
    public Amenity(String amenityId, String name,
                   String description, String status) {
        this.amenityId = amenityId;
        this.name = name;
        this.description = description;
        this.status = status;
    }
 
    public String getAmenityId() {
        return amenityId;
    }
 
    public void setAmenityId(String amenityId) {
        this.amenityId = amenityId;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    private void generateAmenityId() {
        if (amenityId == null || amenityId.isBlank()) {
            amenityId = EntityIdGenerator.generate("AMN");
        }
    }
}
