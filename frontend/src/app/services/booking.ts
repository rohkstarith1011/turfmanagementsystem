import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookingRequestDTO, BookingResponseDTO } from '../models/booking.model';

@Injectable({
  providedIn: 'root'
})
export class Booking {
  private apiUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient) {}

  createBooking(booking: BookingRequestDTO): Observable<BookingResponseDTO> {
    return this.http.post<BookingResponseDTO>(this.apiUrl, booking);
  }

  getPlayerBookings(playerId: string): Observable<BookingResponseDTO[]> {
    return this.http.get<BookingResponseDTO[]>(`${this.apiUrl}/player/${playerId}`);
  }
}
