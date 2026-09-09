export interface TurfRecommendationRequestDTO {
  latitude: number;
  longitude: number;
  maxDistanceKm: number;
  preferredSport: string;
  budgetMin: number;
  budgetMax: number;
  preferredDate: string;
  preferredTime: string;
}

export interface AiTurfRecommendationResponseDTO {
  recommendations: TurfRecommendation[];
}

export interface TurfRecommendation {
  facilityId: string;
  facilityName: string;
  matchScore: number;
  reasons: string[];
  distanceKm: number;
  price: number;
  availability: string;
}

export interface SlotRecommendationRequestDTO {
  facilityId: string;
  preferredDate: string;
  budgetMax: number;
  preferredTimeWindow: string;
}

export interface AiSlotRecommendationResponseDTO {
  recommendations: SlotRecommendation[];
}

export interface SlotRecommendation {
  slotId: string;
  score: number;
  reasons: string[];
  price: number;
  demandLevel: string;
  availability: string;
}

export interface DemandForecastRequestDTO {
  facilityId: string;
  forecastDate: string;
  sport: string;
}

export interface AiDemandForecastResponseDTO {
  forecasts: DemandForecast[];
}

export interface DemandForecast {
  timeWindow: string;
  demandLevel: string; // LOW, MEDIUM, HIGH
  expectedOccupancy: string;
  explanation: string;
}

export interface PricingSimulationRequestDTO {
  facilityId: string;
  simulationDate: string;
}

export interface AiPricingSimulationResponseDTO {
  basePrice: number;
  demandLevel: string;
  recommendedPrice: number;
  minimumPrice: number;
  maximumPrice: number;
  factors: string[];
  approvalStatus: string;
}

export interface PricingApprovalRequestDTO {
  facilityId: string;
  simulationDate: string;
  approvedPrice: number;
}
