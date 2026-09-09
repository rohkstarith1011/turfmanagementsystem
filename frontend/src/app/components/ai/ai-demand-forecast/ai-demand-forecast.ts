import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DemandForecastRequestDTO, DemandForecast } from '../../../models/ai.model';
import { AiService } from '../../../services/ai';
import { Facility } from '../../../services/facility';
import { FacilityResponseDTO } from '../../../models/facility.model';

@Component({
  selector: 'app-ai-demand-forecast',
  templateUrl: './ai-demand-forecast.html',
  styleUrls: ['./ai-demand-forecast.css'],
  standalone: false
})
export class AiDemandForecast implements OnInit {

  facilityId: string = '';
  facility: FacilityResponseDTO | null = null;
  
  request: DemandForecastRequestDTO = {
    facilityId: '',
    forecastDate: '',
    sport: 'Football'
  };

  forecasts: DemandForecast[] = [];
  isLoading = false;
  hasSearched = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private aiService: AiService,
    private facilityService: Facility
  ) { }

  ngOnInit(): void {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.request.forecastDate = tomorrow.toISOString().split('T')[0];

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

  getForecast(): void {
    if (!this.request.facilityId) return;

    this.isLoading = true;
    this.hasSearched = true;
    this.error = '';
    
    this.aiService.forecastDemand(this.request).subscribe({
      next: (res) => {
        this.forecasts = res.forecasts || [];
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to get AI demand forecast: ' + (err.error?.message || err.message);
        this.isLoading = false;
      }
    });
  }
}
