import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FacilityRequestDTO, FacilityResponseDTO } from '../models/facility.model';

@Injectable({
  providedIn: 'root'
})
export class Facility {
  private apiUrl = 'http://localhost:8080/api/facilities';

  constructor(private http: HttpClient) {}

  getAllFacilities(): Observable<FacilityResponseDTO[]> {
    return this.http.get<FacilityResponseDTO[]>(this.apiUrl);
  }

  getFacilityById(id: string): Observable<FacilityResponseDTO> {
    return this.http.get<FacilityResponseDTO>(`${this.apiUrl}/${id}`);
  }

  getFacilitiesByOwner(ownerId: string): Observable<FacilityResponseDTO[]> {
    return this.http.get<FacilityResponseDTO[]>(`${this.apiUrl}/owner/${ownerId}`);
  }

  createFacility(facility: FacilityRequestDTO): Observable<FacilityResponseDTO> {
    return this.http.post<FacilityResponseDTO>(this.apiUrl, facility);
  }

  updateFacility(id: string, facility: FacilityRequestDTO): Observable<FacilityResponseDTO> {
    return this.http.put<FacilityResponseDTO>(`${this.apiUrl}/${id}`, facility);
  }

  // --- Image Operations ---

  uploadImage(facilityId: string, file: File, isPrimary: boolean): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('isPrimary', isPrimary.toString());

    return this.http.post<any>(`${this.apiUrl}/${facilityId}/images`, formData);
  }

  deleteImage(imageId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/images/${imageId}`);
  }

  setPrimaryImage(imageId: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/images/${imageId}/primary`, {});
  }
}
