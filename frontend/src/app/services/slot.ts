import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SlotRequestDTO, SlotResponseDTO } from '../models/slot.model';

@Injectable({
  providedIn: 'root'
})
export class Slot {
  private apiUrl = 'http://localhost:8080/api/slots';

  constructor(private http: HttpClient) {}

  getSlotsByFacilityAndDate(facilityId: string, date: string): Observable<SlotResponseDTO[]> {
    return this.http.get<SlotResponseDTO[]>(`${this.apiUrl}/facility/${facilityId}/date/${date}`);
  }

  createSlot(slot: SlotRequestDTO): Observable<SlotResponseDTO> {
    return this.http.post<SlotResponseDTO>(this.apiUrl, slot);
  }

  // Placeholder for bulk creation which the backend might have via slot-blocks
  createSlotsBulk(slots: SlotRequestDTO[]): Observable<SlotResponseDTO[]> {
    return this.http.post<SlotResponseDTO[]>(`${this.apiUrl}/bulk`, slots);
  }
}
