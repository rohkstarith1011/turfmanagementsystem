package com.crimsonlogic.turfmanagementsystem.entity;



import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long facilityId;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turf_owner_id", nullable = false, unique = true)
    private TurfOwner owner;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turf_manager_id", nullable = false, unique = true)
    private TurfManager manager;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String locality;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String turfType;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rules;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Boolean availability;

    @Column(nullable = false)
    private String status;

    public Facility() {
    }

    public Facility(Long facilityId,
                    String name,
                    TurfOwner owner,
                    TurfManager manager,
                    String location,
                    String address,
                    String locality,
                    String city,
                    String state,
                    String turfType,
                    Integer capacity,
                    LocalTime openingTime,
                    LocalTime closingTime,
                    Double basePrice,
                    String rules,
                    Double rating,
                    Boolean availability,
                    String status) {

        this.facilityId = facilityId;
        this.name = name;
        this.owner = owner;
        this.manager = manager;
        this.location = location;
        this.address = address;
        this.locality = locality;
        this.city = city;
        this.state = state;
        this.turfType = turfType;
        this.capacity = capacity;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.basePrice = basePrice;
        this.rules = rules;
        this.rating = rating;
        this.availability = availability;
        this.status = status;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TurfOwner getOwner() {
        return owner;
    }

    public void setOwner(TurfOwner owner) {
        this.owner = owner;
    }

    public TurfManager getManager() {
        return manager;
    }

    public void setManager(TurfManager manager) {
        this.manager = manager;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTurfType() {
        return turfType;
    }

    public void setTurfType(String turfType) {
        this.turfType = turfType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
