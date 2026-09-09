import { Component, OnInit } from '@angular/core';
import { Booking } from '../../../services/booking';
import { Auth } from '../../../services/auth';
import { BookingResponseDTO } from '../../../models/booking.model';

@Component({
  selector: 'app-my-bookings',
  templateUrl: './my-bookings.html',
  styleUrls: ['./my-bookings.css'],
  standalone: false
})
export class MyBookings implements OnInit {
  bookings: BookingResponseDTO[] = [];
  loading = true;
  error = '';

  constructor(
    private bookingService: Booking,
    private authService: Auth
  ) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  loadBookings(): void {
    const user = this.authService.currentUserValue;
    if (!user || !user.userId) {
      this.error = 'You must be logged in to view your bookings.';
      this.loading = false;
      return;
    }

    this.bookingService.getPlayerBookings(user.userId).subscribe({
      next: (data) => {
        this.bookings = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load bookings. Please try again later.';
        this.loading = false;
      }
    });
  }
}
