import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PricingSimulationRequestDTO, AiPricingSimulationResponseDTO, PricingApprovalRequestDTO } from '../../../models/ai.model';
import { AiService } from '../../../services/ai';
import { Facility } from '../../../services/facility';
import { FacilityResponseDTO } from '../../../models/facility.model';

@Component({
  selector: 'app-ai-pricing-simulation',
  templateUrl: './ai-pricing-simulation.html',
  styleUrls: ['./ai-pricing-simulation.css'],
  standalone: false
})
export class AiPricingSimulation implements OnInit {

  facilityId: string = '';
  facility: FacilityResponseDTO | null = null;
  
  request: PricingSimulationRequestDTO = {
    facilityId: '',
    simulationDate: ''
  };

  simulationResult: AiPricingSimulationResponseDTO | null = null;
  isLoading = false;
  hasSearched = false;
  error = '';
  
  isApproving = false;
  approvalSuccess = '';
  approvalError = '';

  constructor(
    private route: ActivatedRoute,
    private aiService: AiService,
    private facilityService: Facility
  ) { }

  ngOnInit(): void {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.request.simulationDate = tomorrow.toISOString().split('T')[0];

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

  simulatePricing(): void {
    if (!this.request.facilityId) return;

    this.isLoading = true;
    this.hasSearched = true;
    this.error = '';
    this.approvalSuccess = '';
    this.approvalError = '';
    this.simulationResult = null;
    
    this.aiService.simulatePricing(this.request).subscribe({
      next: (res) => {
        this.simulationResult = res;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to run AI pricing simulation: ' + (err.error?.message || err.message);
        this.isLoading = false;
      }
    });
  }

  approvePrice() {
    if (!this.simulationResult) return;

    this.isApproving = true;
    this.approvalError = '';
    this.approvalSuccess = '';

    const approvalReq: PricingApprovalRequestDTO = {
      facilityId: this.facilityId,
      simulationDate: this.request.simulationDate,
      approvedPrice: this.simulationResult.recommendedPrice
    };

    this.aiService.approvePricing(approvalReq).subscribe({
      next: (res) => {
        this.approvalSuccess = 'Pricing approved and updated successfully!';
        this.isApproving = false;
        if (this.simulationResult) {
          this.simulationResult.approvalStatus = 'APPROVED';
        }
        this.loadFacility(); // reload to get new base price
      },
      error: (err) => {
        this.approvalError = 'Failed to approve pricing: ' + (err.error?.message || err.message);
        this.isApproving = false;
      }
    });
  }

  rejectPrice() {
    if (this.simulationResult) {
      this.simulationResult.approvalStatus = 'REJECTED';
    }
  }
}
