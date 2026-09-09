import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Slot } from '../../../services/slot';
import { SlotRequestDTO, SlotResponseDTO } from '../../../models/slot.model';

@Component({
  selector: 'app-slot-management',
  templateUrl: './slot-management.html',
  styleUrls: ['./slot-management.css'],
  standalone: false
})
export class SlotManagement implements OnInit {
  facilityId: string = '';
  
  model: SlotRequestDTO = {
    facilityId: '',
    playingAreaId: '', // Assume a default or fetch from facility if required
    slotDate: '',
    startTime: '',
    endTime: '',
    price: 50
  };

  viewDate: string = new Date().toISOString().split('T')[0];
  slots: SlotResponseDTO[] = [];
  
  loading = false;
  slotsLoading = false;
  error = '';
  successMsg = '';

  constructor(
    private route: ActivatedRoute,
    private slotService: Slot
  ) {}

  ngOnInit(): void {
    this.facilityId = this.route.snapshot.paramMap.get('id') || '';
    this.model.facilityId = this.facilityId;
    this.model.slotDate = this.viewDate;
    
    // In a real app we would fetch the PlayingArea list for this facility, 
    // for MVP we can hardcode a placeholder or expect backend to handle it if optional.
    // For now we set it to a dummy UUID or let the backend fail validation gracefully
    this.model.playingAreaId = '00000000-0000-0000-0000-000000000000'; // Needs real ID

    this.loadSlots();
  }

  loadSlots(): void {
    if (!this.facilityId || !this.viewDate) return;
    
    this.slotsLoading = true;
    this.slotService.getSlotsByFacilityAndDate(this.facilityId, this.viewDate).subscribe({
      next: (data) => {
        this.slots = data;
        this.slotsLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.slotsLoading = false;
      }
    });
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.successMsg = '';

    const submitModel = { ...this.model };
    if (submitModel.startTime.length === 5) submitModel.startTime += ':00';
    if (submitModel.endTime.length === 5) submitModel.endTime += ':00';

    this.slotService.createSlot(submitModel).subscribe({
      next: () => {
        this.successMsg = 'Slot created successfully!';
        this.loading = false;
        if (this.viewDate === this.model.slotDate) {
          this.loadSlots();
        }
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create slot.';
        this.loading = false;
      }
    });
  }
}
