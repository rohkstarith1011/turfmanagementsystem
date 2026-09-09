package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;
import java.util.List;
public class AdminDashboardResponseDTO {

    private Long totalUsers;
    private Long totalTurfs;
    private Long activeTurfs;
    private Long totalBookings;
    private Double revenue;
    private Long cancellations;
    private List<String> popularLocations;
    private List<String> popularSports;
    private List<String> peakBookingHours;
    private Double occupancyRate;
    private List<OwnerPerformanceDTO> ownerPerformance;
    private List<FacilityPerformanceDTO> facilityPerformance;
    public AdminDashboardResponseDTO(
            Long totalUsers,
            Long totalTurfs,
            Long activeTurfs,
            Long totalBookings,
            Double revenue,
            Long cancellations,
            List<String> popularLocations,
            List<String> popularSports,
            List<String> peakBookingHours,
            Double occupancyRate,
            List<OwnerPerformanceDTO> ownerPerformance,
            List<FacilityPerformanceDTO> facilityPerformance)   {
        this.totalUsers = totalUsers;
        this.totalTurfs = totalTurfs;
        this.activeTurfs = activeTurfs;
        this.totalBookings = totalBookings;
        this.revenue = revenue;
        this.cancellations = cancellations;
        this.popularLocations = popularLocations;
        this.popularSports = popularSports;
        this.peakBookingHours = peakBookingHours;
        this.occupancyRate = occupancyRate;
        this.ownerPerformance = ownerPerformance;
        this.facilityPerformance = facilityPerformance;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public Long getTotalTurfs() {
        return totalTurfs;
    }

    public Long getActiveTurfs() {
        return activeTurfs;
    }

    public Long getTotalBookings() {
        return totalBookings;
    }

    public Double getRevenue() {
        return revenue;
    }

    public Long getCancellations() {
        return cancellations;
    }
    
    public List<String> getPopularLocations() {
        return popularLocations;
    }
    public List<String> getPopularSports() {
        return popularSports;
    }
    
    public List<String> getPeakBookingHours() {
        return peakBookingHours;
    }
    public Double getOccupancyRate() {
        return occupancyRate;
    }
    public List<OwnerPerformanceDTO> getOwnerPerformance() {
        return ownerPerformance;
    }
    public List<FacilityPerformanceDTO> getFacilityPerformance() {
        return facilityPerformance;
    }
}