import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Facility } from '../../../services/facility';
import { Slot } from '../../../services/slot';
import { Booking } from '../../../services/booking';
import { Auth } from '../../../services/auth';
import { FacilityResponseDTO } from '../../../models/facility.model';
import { SlotResponseDTO } from '../../../models/slot.model';
import { BookingRequestDTO } from '../../../models/booking.model';

@Component({
  selector: 'app-facility-view',
  templateUrl: './facility-view.html',
  styleUrls: ['./facility-view.css'],
  standalone: false
})
export class FacilityView implements OnInit {
  facilityId: string = '';
  facility: FacilityResponseDTO | null = null;
  loading = true;

  selectedDate: string = new Date().toISOString().split('T')[0];
  slots: SlotResponseDTO[] = [];
  loadingSlots = false;

  selectedSlot: SlotResponseDTO | null = null;
  bookingInProgress = false;
  bookingError = '';
  bookingSuccess = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private facilityService: Facility,
    private slotService: Slot,
    private bookingService: Booking,
    private authService: Auth
  ) {}

  ngOnInit(): void {
    this.facilityId = this.route.snapshot.paramMap.get('id') || '';
    if (this.facilityId) {
      this.loadFacilityDetails();
      this.loadSlots();
    }
  }

  loadFacilityDetails(): void {
    this.facilityService.getFacilityById(this.facilityId).subscribe({
      next: (data) => {
        this.facility = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  loadSlots(): void {
    if (!this.facilityId || !this.selectedDate) return;
    
    this.loadingSlots = true;
    this.selectedSlot = null; // Reset selection
    this.bookingSuccess = '';
    this.bookingError = '';

    this.slotService.getSlotsByFacilityAndDate(this.facilityId, this.selectedDate).subscribe({
      next: (data) => {
        this.slots = data;
        this.loadingSlots = false;
      },
      error: (err) => {
        console.error(err);
        this.loadingSlots = false;
      }
    });
  }

  selectSlot(slot: SlotResponseDTO): void {
    if (slot.status === 'AVAILABLE') {
      this.selectedSlot = slot;
      this.bookingSuccess = '';
      this.bookingError = '';
    }
  }

  bookSlot(): void {
    if (!this.selectedSlot) return;

    const user = this.authService.currentUserValue;
    if (!user || !user.userId) {
      this.bookingError = 'You must be logged in to book a slot.';
      return;
    }

    this.bookingInProgress = true;
    this.bookingError = '';

    const req: BookingRequestDTO = {
      playerId: user.userId,
      slotId: this.selectedSlot.slotId
    };

    this.bookingService.createBooking(req).subscribe({
      next: (response) => {
        this.bookingSuccess = `Booking successful! Booking ID: ${response.bookingId}`;
        this.bookingInProgress = false;
        this.selectedSlot = null;
        this.loadSlots(); // Refresh slot availability
        
        // Navigate to my-bookings after a short delay
        setTimeout(() => {
          this.router.navigate(['/player/bookings']);
        }, 2000);
      },
      error: (err) => {
        this.bookingError = err.error?.message || 'Failed to confirm booking. Someone might have booked it just now.';
        this.bookingInProgress = false;
      }
    });
  }
}
