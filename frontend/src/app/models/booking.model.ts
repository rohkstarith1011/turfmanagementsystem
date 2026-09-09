export interface BookingRequestDTO {
  playerId: string;
  slotId: string;
}

export interface BookingResponseDTO {
  bookingId: string;
  slotId: string;
  playerId: string;
  playerName: string;
  facilityName: string;
  slotDate: string;
  startTime: string;
  endTime: string;
  totalAmount: number;
  status: string;
  bookingTime: string;
}
