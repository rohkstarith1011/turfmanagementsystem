export interface FacilityRequestDTO {
  ownerId: string;
  name: string;
  description: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  contactNumber: string;
  openingTime: string; // HH:mm:ss
  closingTime: string; // HH:mm:ss
  latitude?: number;
  longitude?: number;
  isActive: boolean;
}

export interface FacilityResponseDTO {
  facilityId: string;
  ownerId: string;
  ownerName: string;
  name: string;
  description: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  contactNumber: string;
  openingTime: string;
  closingTime: string;
  latitude: number;
  longitude: number;
  isActive: boolean;
  status: string;
  averageRating: number;
  images?: {
    imageId: string;
    imageUrl: string;
    isPrimary: boolean;
  }[];
}
