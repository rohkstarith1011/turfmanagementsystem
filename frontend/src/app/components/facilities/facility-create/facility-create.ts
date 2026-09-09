import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Facility } from '../../../services/facility';
import { Auth } from '../../../services/auth';
import { FacilityRequestDTO } from '../../../models/facility.model';

@Component({
  selector: 'app-facility-create',
  templateUrl: './facility-create.html',
  styleUrls: ['./facility-create.css'],
  standalone: false
})
export class FacilityCreate implements OnInit {
  model: FacilityRequestDTO = {
    ownerId: '',
    name: '',
    description: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    contactNumber: '',
    openingTime: '06:00', // Default
    closingTime: '23:00', // Default
    isActive: true
  };
  
  loading = false;
  error = '';

  constructor(
    private facilityService: Facility,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.currentUserValue;
    if (user && user.userId) {
      this.model.ownerId = user.userId;
    }
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';

    // Append seconds to time strings as backend expects HH:mm:ss if it's LocalTime
    const submitModel = { ...this.model };
    if (submitModel.openingTime.length === 5) submitModel.openingTime += ':00';
    if (submitModel.closingTime.length === 5) submitModel.closingTime += ':00';

    this.facilityService.createFacility(submitModel).subscribe({
      next: (response) => {
        // Redirect to image upload page for this new facility
        this.router.navigate(['/facilities', response.facilityId, 'images']);
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create facility.';
        this.loading = false;
      }
    });
  }
}
