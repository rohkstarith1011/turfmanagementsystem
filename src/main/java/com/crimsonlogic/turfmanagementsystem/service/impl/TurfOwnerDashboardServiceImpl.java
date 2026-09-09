package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerDashboardResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Booking;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.Slot;
import com.crimsonlogic.turfmanagementsystem.entity.TurfOwner;
import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.turfmanagementsystem.repository.BookingRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.PaymentRepository;
import com.crimsonlogic.turfmanagementsystem.repository.SlotRepository;
import com.crimsonlogic.turfmanagementsystem.repository.TurfOwnerRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfOwnerDashboardService;

@Service
@Transactional(readOnly = true)
public class TurfOwnerDashboardServiceImpl implements ITurfOwnerDashboardService {

    private final TurfOwnerRepository turfOwnerRepository;
    private final FacilityRepository facilityRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final SlotRepository slotRepository;

    public TurfOwnerDashboardServiceImpl(
            TurfOwnerRepository turfOwnerRepository,
            FacilityRepository facilityRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            SlotRepository slotRepository) {

        this.turfOwnerRepository = turfOwnerRepository;
        this.facilityRepository = facilityRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public TurfOwnerDashboardResponseDTO getDashboard(String ownerId) {

        TurfOwner owner = turfOwnerRepository.findById(ownerId)
                .orElseThrow(() ->
                new ResourceNotFoundException("Turf owner not found: " + ownerId));

        Facility facility = facilityRepository
                .findByOwnerTurfOwnerId(owner.getTurfOwnerId())
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No facility found for turf owner: " + ownerId));

        String facilityId = facility.getFacilityId();

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        Long todayBookings =
                bookingRepository.countBookingsByFacilityAndDate(
                        facilityId, today);

        Long upcomingBookings =
                bookingRepository.countUpcomingBookingsByFacility(
                        facilityId, today, currentTime);

        Long totalBookings =
                bookingRepository.countTotalBookingsByFacility(facilityId);

        Long cancelledBookings =
                bookingRepository.countCancelledBookingsByFacility(facilityId);

        Long noShowBookings =
                bookingRepository.countNoShowBookingsByFacility(facilityId);

        Double occupancyRate =
                calculateOccupancyRate(facilityId, today);

        Double revenue =
                paymentRepository.calculateRevenueByFacility(facilityId);

        Double cancellationRate =
                calculateRate(cancelledBookings, totalBookings);

        Double noShowRate =
                calculateRate(noShowBookings, totalBookings);

        List<Booking> allBookings =
                bookingRepository.findAllBookingsByFacility(facilityId);

        List<String> popularSports =
                calculatePopularSports(allBookings);

        List<String> peakHours =
                calculatePeakHours(allBookings);

        Double customerRating =
                facility.getRating() == null
                        ? 0.0
                        : facility.getRating();

        return new TurfOwnerDashboardResponseDTO(
                owner.getTurfOwnerId(),
                facility.getFacilityId(),
                facility.getName(),
                todayBookings,
                upcomingBookings,
                occupancyRate,
                revenue,
                cancellationRate,
                noShowRate,
                popularSports,
                peakHours,
                customerRating);
    }

    private Double calculateOccupancyRate(
            String facilityId,
            LocalDate date) {

        Long totalActiveSlots =
                slotRepository.countActiveSlotsByFacilityAndDate(
                        facilityId, date);

        if (totalActiveSlots == null || totalActiveSlots == 0) {
            return 0.0;
        }

        Long occupiedBookings =
                bookingRepository.countOccupiedBookingsByFacilityAndDate(
                        facilityId, date);

        double occupancy =
                ((double) occupiedBookings / totalActiveSlots) * 100.0;

        return roundToTwoDecimals(occupancy);
    }

    private Double calculateRate(Long count, Long total) {

        if (total == null || total == 0) {
            return 0.0;
        }

        double rate =
                ((double) count / total) * 100.0;

        return roundToTwoDecimals(rate);
    }

    private List<String> calculatePopularSports(
            List<Booking> bookings) {

        Map<String, Long> sportCounts = new LinkedHashMap<>();

        for (Booking booking : bookings) {

            if (booking.getStatus() == BookingStatus.CANCELLED
                    || booking.getStatus() == BookingStatus.NO_SHOW
                    || booking.getSlot() == null
                    || booking.getSlot().getPlayingArea() == null
                    || booking.getSlot().getPlayingArea().getTurfSport() == null
                    || booking.getSlot().getPlayingArea()
                            .getTurfSport().getSport() == null) {
                continue;
            }

            String sportName = booking.getSlot()
                    .getPlayingArea()
                    .getTurfSport()
                    .getSport()
                    .getName();

            if (sportName != null) {
                sportCounts.merge(sportName, 1L, Long::sum);
            }
        }

        return sportCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<String> calculatePeakHours(
            List<Booking> bookings) {

        Map<String, Long> hourCounts = new LinkedHashMap<>();

        for (Booking booking : bookings) {

            if (booking.getStartTime() == null
                    || booking.getStatus() == BookingStatus.CANCELLED
                    || booking.getStatus() == BookingStatus.NO_SHOW) {
                continue;
            }

            String hour = booking.getStartTime()
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toString();

            hourCounts.merge(hour, 1L, Long::sum);
        }

        if (hourCounts.isEmpty()) {
            return new ArrayList<>();
        }

        Long maxCount = hourCounts.values()
                .stream()
                .max(Long::compareTo)
                .orElse(0L);

        return hourCounts.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(maxCount))
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    private Double roundToTwoDecimals(Double value) {

        return Math.round(value * 100.0) / 100.0;
    }
}