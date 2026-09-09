import { Component, OnInit } from '@angular/core';
import { Facility } from '../../../services/facility';
import { Auth } from '../../../services/auth';
import { FacilityResponseDTO } from '../../../models/facility.model';

@Component({
  selector: 'app-facility-list',
  templateUrl: './facility-list.html',
  styleUrls: ['./facility-list.css'],
  standalone: false
})
export class FacilityList implements OnInit {
  facilities: FacilityResponseDTO[] = [];
  loading = true;
  error = '';

  constructor(
    private facilityService: Facility,
    public authService: Auth
  ) {}

  ngOnInit(): void {
    this.loadFacilities();
  }

  loadFacilities(): void {
    const user = this.authService.currentUserValue;
    if (!user || !user.userId) {
      this.error = 'User not authenticated.';
      this.loading = false;
      return;
    }

    if (this.authService.hasRole('OWNER')) {
      this.facilityService.getFacilitiesByOwner(user.userId).subscribe({
        next: (data) => {
          this.facilities = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load facilities. Please try again later.';
          this.loading = false;
          console.error(err);
        }
      });
    } else {
      this.facilityService.getAllFacilities().subscribe({
        next: (data) => {
          this.facilities = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load facilities. Please try again later.';
          this.loading = false;
          console.error(err);
        }
      });
    }
  }
}
