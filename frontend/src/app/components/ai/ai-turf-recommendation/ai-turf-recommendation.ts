import { Component, OnInit } from '@angular/core';
import { TurfRecommendationRequestDTO, TurfRecommendation } from '../../../models/ai.model';
import { AiService } from '../../../services/ai';

@Component({
  selector: 'app-ai-turf-recommendation',
  templateUrl: './ai-turf-recommendation.html',
  styleUrls: ['./ai-turf-recommendation.css'],
  standalone: false
})
export class AiTurfRecommendation implements OnInit {

  request: TurfRecommendationRequestDTO = {
    latitude: 40.7128, // Defaulting to somewhere since GPS is not implemented
    longitude: -74.0060,
    maxDistanceKm: 20,
    preferredSport: 'Football',
    budgetMin: 0,
    budgetMax: 100,
    preferredDate: '',
    preferredTime: ''
  };

  recommendations: TurfRecommendation[] = [];
  isLoading = false;
  hasSearched = false;
  error = '';

  constructor(private aiService: AiService) { }

  ngOnInit(): void {
    const today = new Date();
    this.request.preferredDate = today.toISOString().split('T')[0];
  }

  getRecommendations(): void {
    this.isLoading = true;
    this.hasSearched = true;
    this.error = '';
    
    this.aiService.recommendTurfs(this.request).subscribe({
      next: (res) => {
        this.recommendations = res.recommendations || [];
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to get AI recommendations: ' + (err.error?.message || err.message);
        this.isLoading = false;
      }
    });
  }
}
