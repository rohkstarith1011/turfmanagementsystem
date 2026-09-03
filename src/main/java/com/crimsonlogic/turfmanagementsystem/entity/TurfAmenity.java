package com.crimsonlogic.turfmanagementsystem.entity;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
 
@Entity
@Table(
    name = "turf_amenity",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_facility_amenity",
        columnNames = {"facility_id", "amenity_id"}
    )
)
public class TurfAmenity {
 
    @Id
    @Column(name = "turf_amenity_id")
    private String turfAmenityId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;
 
    @Column(nullable = false)
    private String status;
 
    public TurfAmenity() {
    }
 
    public TurfAmenity(String turfAmenityId, Facility facility,
                       Amenity amenity, String status) {
        this.turfAmenityId = turfAmenityId;
        this.facility = facility;
        this.amenity = amenity;
        this.status = status;
    }
 
    public String getTurfAmenityId() {
        return turfAmenityId;
    }
 
    public void setTurfAmenityId(String turfAmenityId) {
        this.turfAmenityId = turfAmenityId;
    }
 
    public Facility getFacility() {
        return facility;
    }
 
    public void setFacility(Facility facility) {
        this.facility = facility;
    }
 
    public Amenity getAmenity() {
        return amenity;
    }
 
    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    private void generateTurfAmenityId() {
        if (turfAmenityId == null || turfAmenityId.isBlank()) {
            turfAmenityId = EntityIdGenerator.generate("TFAMN");
        }
    }
}
