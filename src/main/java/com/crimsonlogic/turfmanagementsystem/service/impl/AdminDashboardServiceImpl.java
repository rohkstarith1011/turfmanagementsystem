package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AdminDashboardResponseDTO;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PaymentRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.projection.PeakBookingHourProjection;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAdminDashboardService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.OwnerPerformanceDTO;
import com.crimsonlogic.turfmanagementsystem.repository.projection.OwnerPerformanceProjection;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.FacilityPerformanceDTO;

@Service
public class AdminDashboardServiceImpl implements IAdminDashboardService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final SlotRepository slotRepository;
    public AdminDashboardServiceImpl(
            UserRepository userRepository,
            FacilityRepository facilityRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            SlotRepository slotRepository) {

        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public AdminDashboardResponseDTO getDashboard() {

        Long totalUsers = userRepository.count();

        Long totalTurfs = facilityRepository.count();

        Long activeTurfs =
                facilityRepository.countByStatusIgnoreCase("ACTIVE");

        Long totalBookings = bookingRepository.count();

        Double revenue = paymentRepository.calculateTotalRevenue();

        Long cancellations = bookingRepository.countCancelledBookings();
        Long totalActiveSlots = slotRepository.countAllActiveSlots();
        Long occupiedBookings = bookingRepository.countOccupiedBookings();

        Double occupancyRate = 0.0;

        if (totalActiveSlots != null && totalActiveSlots > 0) {
            occupancyRate = (occupiedBookings.doubleValue() / totalActiveSlots.doubleValue()) * 100;
        }
        List<String> popularLocations =
                bookingRepository.findPopularLocations()
                        .stream()
                        .map(location -> location.getLocation())
                        .collect(Collectors.toList());
        List<String> popularSports =
                bookingRepository.findPopularSports()
                        .stream()
                        .map(sport -> sport.getSportName())
                        .collect(Collectors.toList());
        List<PeakBookingHourProjection> peakHourResults =
                bookingRepository.findPeakBookingHours();
        List<String> peakBookingHours = new java.util.ArrayList<>();
        List<OwnerPerformanceDTO> ownerPerformance =
                bookingRepository.findOwnerPerformance()
                        .stream()
                        .map(owner -> new OwnerPerformanceDTO(
                                owner.getOwnerName(),
                                owner.getFacilityName(),
                                owner.getTotalBookings(),
                                owner.getRevenue()
                        ))
                        .collect(Collectors.toList());
        List<FacilityPerformanceDTO> facilityPerformance =
                bookingRepository.findFacilityPerformance()
                        .stream()
                        .map(facility -> new FacilityPerformanceDTO(
                                facility.getFacilityName(),
                                facility.getLocation(),
                                facility.getTotalBookings(),
                                facility.getRevenue(),
                                facility.getRating()
                        ))
                        .collect(Collectors.toList());
        if (!peakHourResults.isEmpty()) {

            Long maxBookingCount = peakHourResults.get(0).getBookingCount();

            peakBookingHours = peakHourResults.stream()
                    .filter(hour -> hour.getBookingCount().equals(maxBookingCount))
                    .map(hour -> hour.getBookingHour())
                    .sorted()
                    .collect(Collectors.toList());
        }
        
        
        return new AdminDashboardResponseDTO(
                totalUsers,
                totalTurfs,
                activeTurfs,
                totalBookings,
                revenue,
                cancellations,
                popularLocations,
                popularSports,
                peakBookingHours,
                occupancyRate,
                ownerPerformance,
                facilityPerformance
        );
    }
}