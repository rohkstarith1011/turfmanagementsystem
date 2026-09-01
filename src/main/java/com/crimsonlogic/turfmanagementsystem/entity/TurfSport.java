package com.crimsonlogic.turfmanagementsystem.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "turf_sport",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_facility_sport",
            columnNames = {"facility_id", "sport_id"}
        )
    }
)
public class TurfSport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turf_sport_id")
    private Long turfSportId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(nullable = false)
    private String status;

    public TurfSport() {
    }

    public TurfSport(Long turfSportId,
                     Facility facility,
                     Sport sport,
                     String status) {

        this.turfSportId = turfSportId;
        this.facility = facility;
        this.sport = sport;
        this.status = status;
    }

    public Long getTurfSportId() {
        return turfSportId;
    }

    public void setTurfSportId(Long turfSportId) {
        this.turfSportId = turfSportId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
