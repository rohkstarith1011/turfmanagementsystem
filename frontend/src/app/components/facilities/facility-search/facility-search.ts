import { Component, OnInit } from '@angular/core';
import { Facility } from '../../../services/facility';
import { FacilityResponseDTO } from '../../../models/facility.model';

@Component({
  selector: 'app-facility-search',
  templateUrl: './facility-search.html',
  styleUrls: ['./facility-search.css'],
  standalone: false
})
export class FacilitySearch implements OnInit {
  facilities: FacilityResponseDTO[] = [];
  searchQuery: string = '';
  loading = true;
  error = '';

  constructor(private facilityService: Facility) {}

  ngOnInit(): void {
    this.search();
  }

  search(): void {
    this.loading = true;
    this.error = '';
    
    // Using getAllFacilities as a simplistic search for now
    // In a real app, this would hit a search endpoint with query params
    this.facilityService.getAllFacilities().subscribe({
      next: (data) => {
        // Simple client-side filtering if search query exists
        if (this.searchQuery && this.searchQuery.trim() !== '') {
          const query = this.searchQuery.toLowerCase();
          this.facilities = data.filter(f => 
            f.name.toLowerCase().includes(query) || 
            f.city.toLowerCase().includes(query)
          );
        } else {
          this.facilities = data;
        }
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to fetch facilities.';
        this.loading = false;
      }
    });
  }
}
