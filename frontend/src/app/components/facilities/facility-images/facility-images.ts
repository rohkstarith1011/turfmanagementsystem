import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Facility } from '../../../services/facility';
import { FacilityResponseDTO } from '../../../models/facility.model';

@Component({
  selector: 'app-facility-images',
  templateUrl: './facility-images.html',
  styleUrls: ['./facility-images.css'],
  standalone: false
})
export class FacilityImages implements OnInit {
  facilityId: string = '';
  facility: FacilityResponseDTO | null = null;
  loading = true;
  
  selectedFile: File | null = null;
  isPrimary = false;
  uploading = false;
  uploadError = '';
  uploadSuccess = '';

  constructor(
    private route: ActivatedRoute,
    private facilityService: Facility
  ) {}

  ngOnInit(): void {
    this.facilityId = this.route.snapshot.paramMap.get('id') || '';
    if (this.facilityId) {
      this.loadFacility();
    }
  }

  loadFacility(): void {
    this.loading = true;
    this.facilityService.getFacilityById(this.facilityId).subscribe({
      next: (data) => {
        this.facility = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  uploadImage(): void {
    if (!this.selectedFile || !this.facilityId) return;
    
    this.uploading = true;
    this.uploadError = '';
    this.uploadSuccess = '';

    this.facilityService.uploadImage(this.facilityId, this.selectedFile, this.isPrimary).subscribe({
      next: () => {
        this.uploadSuccess = 'Image uploaded successfully!';
        this.uploading = false;
        this.selectedFile = null;
        this.isPrimary = false;
        // Reset file input
        const fileInput = document.getElementById('imageFile') as HTMLInputElement;
        if (fileInput) fileInput.value = '';
        
        this.loadFacility(); // Reload images
      },
      error: (err) => {
        this.uploadError = err.error?.message || 'Failed to upload image.';
        this.uploading = false;
      }
    });
  }

  deleteImage(imageId: string): void {
    if(confirm('Are you sure you want to delete this image?')) {
      this.facilityService.deleteImage(imageId).subscribe({
        next: () => {
          this.loadFacility();
        },
        error: (err) => {
          console.error('Failed to delete image', err);
        }
      });
    }
  }

  setPrimary(imageId: string): void {
    this.facilityService.setPrimaryImage(imageId).subscribe({
      next: () => {
        this.loadFacility();
      },
      error: (err) => {
        console.error('Failed to set primary image', err);
      }
    });
  }
}
