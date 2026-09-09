import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  TurfRecommendationRequestDTO, 
  AiTurfRecommendationResponseDTO,
  SlotRecommendationRequestDTO,
  AiSlotRecommendationResponseDTO,
  DemandForecastRequestDTO,
  AiDemandForecastResponseDTO,
  PricingSimulationRequestDTO,
  AiPricingSimulationResponseDTO,
  PricingApprovalRequestDTO
} from '../models/ai.model';

@Injectable({
  providedIn: 'root'
})
export class AiService {

  private baseUrl = '/api/ai';

  constructor(private http: HttpClient) { }

  recommendTurfs(request: TurfRecommendationRequestDTO): Observable<AiTurfRecommendationResponseDTO> {
    return this.http.post<AiTurfRecommendationResponseDTO>(`${this.baseUrl}/recommendations/turf`, request);
  }

  recommendSlots(request: SlotRecommendationRequestDTO): Observable<AiSlotRecommendationResponseDTO> {
    return this.http.post<AiSlotRecommendationResponseDTO>(`${this.baseUrl}/recommendations/slot`, request);
  }

  forecastDemand(request: DemandForecastRequestDTO): Observable<AiDemandForecastResponseDTO> {
    return this.http.post<AiDemandForecastResponseDTO>(`${this.baseUrl}/forecast/demand`, request);
  }

  simulatePricing(request: PricingSimulationRequestDTO): Observable<AiPricingSimulationResponseDTO> {
    return this.http.post<AiPricingSimulationResponseDTO>(`${this.baseUrl}/pricing/simulate`, request);
  }

  approvePricing(request: PricingApprovalRequestDTO): Observable<string> {
    return this.http.post(`${this.baseUrl}/pricing/approve`, request, { responseType: 'text' });
  }
}
