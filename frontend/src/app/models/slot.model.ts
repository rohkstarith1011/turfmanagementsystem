export interface SlotResponseDTO {
  slotId: string;
  facilityId: string;
  playingAreaId: string;
  slotDate: string; // YYYY-MM-DD
  startTime: string; // HH:mm:ss
  endTime: string; // HH:mm:ss
  price: number;
  status: string;
}

export interface SlotRequestDTO {
  facilityId: string;
  playingAreaId: string;
  slotDate: string;
  startTime: string;
  endTime: string;
  price: number;
}
