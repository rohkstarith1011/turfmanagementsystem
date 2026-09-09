package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import java.util.List;

public class TurfOwnerDashboardResponseDTO {

    private String ownerId;
    private String facilityId;
    private String facilityName;

    private Long todayBookings;
    private Long upcomingBookings;

    private Double occupancyRate;

    private Double revenue;

    private Double cancellationRate;
    private Double noShowRate;

    private List<String> popularSports;
    private List<String> peakHours;

    private Double customerRating;

    public TurfOwnerDashboardResponseDTO() {
    }

    public TurfOwnerDashboardResponseDTO(
            String ownerId,
            String facilityId,
            String facilityName,
            Long todayBookings,
            Long upcomingBookings,
            Double occupancyRate,
            Double revenue,
            Double cancellationRate,
            Double noShowRate,
            List<String> popularSports,
            List<String> peakHours,
            Double customerRating) {

        this.ownerId = ownerId;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.todayBookings = todayBookings;
        this.upcomingBookings = upcomingBookings;
        this.occupancyRate = occupancyRate;
        this.revenue = revenue;
        this.cancellationRate = cancellationRate;
        this.noShowRate = noShowRate;
        this.popularSports = popularSports;
        this.peakHours = peakHours;
        this.customerRating = customerRating;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Long getTodayBookings() {
        return todayBookings;
    }

    public void setTodayBookings(Long todayBookings) {
        this.todayBookings = todayBookings;
    }

    public Long getUpcomingBookings() {
        return upcomingBookings;
    }

    public void setUpcomingBookings(Long upcomingBookings) {
        this.upcomingBookings = upcomingBookings;
    }

    public Double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(Double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getCancellationRate() {
        return cancellationRate;
    }

    public void setCancellationRate(Double cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public Double getNoShowRate() {
        return noShowRate;
    }

    public void setNoShowRate(Double noShowRate) {
        this.noShowRate = noShowRate;
    }

    public List<String> getPopularSports() {
        return popularSports;
    }

    public void setPopularSports(List<String> popularSports) {
        this.popularSports = popularSports;
    }

    public List<String> getPeakHours() {
        return peakHours;
    }

    public void setPeakHours(List<String> peakHours) {
        this.peakHours = peakHours;
    }

    public Double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Double customerRating) {
        this.customerRating = customerRating;
    }
}