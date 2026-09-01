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
    name = "playing_area",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_turf_sport_playing_area_name",
            columnNames = {"turf_sport_id", "name"}
        )
    }
)
public class PlayingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playing_area_id")
    private Long playingAreaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turf_sport_id", nullable = false)
    private TurfSport turfSport;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status;

    public PlayingArea() {
    }

    public PlayingArea(Long playingAreaId,
                       Facility facility,
                       TurfSport turfSport,
                       String name,
                       String description,
                       String status) {

        this.playingAreaId = playingAreaId;
        this.facility = facility;
        this.turfSport = turfSport;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Long getPlayingAreaId() {
        return playingAreaId;
    }

    public void setPlayingAreaId(Long playingAreaId) {
        this.playingAreaId = playingAreaId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public TurfSport getTurfSport() {
        return turfSport;
    }

    public void setTurfSport(TurfSport turfSport) {
        this.turfSport = turfSport;
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
}