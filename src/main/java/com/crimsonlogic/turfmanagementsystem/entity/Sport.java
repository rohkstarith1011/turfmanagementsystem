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
@Table(name = "sport")
public class Sport {

    @Id
    @Column(name = "sport_id")
    private String sportId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String status;

    public Sport() {
    }

    public Sport(String sportId,
                 String name,
                 String description,
                 String status) {

        this.sportId = sportId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
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
    private void generateSportId() {
        if (sportId == null || sportId.isBlank()) {
            sportId = EntityIdGenerator.generate("SPR");
        }
    }
}