export interface UserResponseDTO {
  userId: string;
  name: string;
  email: string;
  phone: string;
  status: string;
}

export interface UserRequestDTO {
  name: string;
  email: string;
  phone: string;
  password?: string;
}
