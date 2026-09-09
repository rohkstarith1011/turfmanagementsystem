import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SlotRecommendationRequestDTO, SlotRecommendation } from '../../../models/ai.model';
import { AiService } from '../../../services/ai';
import { Facility } from '../../../services/facility';
import { FacilityResponseDTO } from '../../../models/facility.model';
import { Auth } from '../../../services/auth';
import { BookingRequestDTO } from '../../../models/booking.model';
import { Booking } from '../../../services/booking';

@Component({
  selector: 'app-ai-slot-recommendation',
  templateUrl: './ai-slot-recommendation.html',
  styleUrls: ['./ai-slot-recommendation.css'],
  standalone: false
})
export class AiSlotRecommendation implements OnInit {

  facilityId: string = '';
  facility: FacilityResponseDTO | null = null;
  
  request: SlotRecommendationRequestDTO = {
    facilityId: '',
    preferredDate: '',
    budgetMax: 100,
    preferredTimeWindow: 'Morning'
  };

  recommendations: SlotRecommendation[] = [];
  isLoading = false;
  hasSearched = false;
  error = '';
  bookingInProgress = false;
  bookingSuccess = '';
  bookingError = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private aiService: AiService,
    private facilityService: Facility,
    private bookingService: Booking,
    private authService: Auth
  ) { }

  ngOnInit(): void {
    const today = new Date();
    this.request.preferredDate = today.toISOString().split('T')[0];

    this.route.queryParams.subscribe(params => {
      if (params['facilityId']) {
        this.facilityId = params['facilityId'];
        this.request.facilityId = this.facilityId;
        this.loadFacility();
      } else {
        this.error = 'No facility ID provided.';
      }
    });
  }

  loadFacility() {
    this.facilityService.getFacilityById(this.facilityId).subscribe({
      next: (res) => {
        this.facility = res;
      },
      error: (err) => {
        this.error = 'Failed to load facility details.';
      }
    });
  }

  getRecommendations(): void {
    if (!this.request.facilityId) return;

    this.isLoading = true;
    this.hasSearched = true;
    this.error = '';
    this.bookingSuccess = '';
    this.bookingError = '';
    
    this.aiService.recommendSlots(this.request).subscribe({
      next: (res) => {
        this.recommendations = res.recommendations || [];
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to get AI slot recommendations: ' + (err.error?.message || err.message);
        this.isLoading = false;
      }
    });
  }

  bookSlot(slotId: string) {
    this.bookingInProgress = true;
    this.bookingError = '';
    this.bookingSuccess = '';

    const user = this.authService.currentUserValue;
    if (!user || !user.userId) {
      this.bookingError = 'You must be logged in to book.';
      this.bookingInProgress = false;
      return;
    }

    const bookingRequest: BookingRequestDTO = {
      playerId: user.userId,
      slotId: slotId
    };

    this.bookingService.createBooking(bookingRequest).subscribe({
      next: (res) => {
        this.bookingSuccess = 'Slot booked successfully! Booking ID: ' + res.bookingId;
        this.bookingInProgress = false;
        setTimeout(() => {
          this.router.navigate(['/player/bookings']);
        }, 2000);
      },
      error: (err) => {
        this.bookingError = 'Booking failed: ' + (err.error?.message || err.message);
        this.bookingInProgress = false;
      }
    });
  }
}
