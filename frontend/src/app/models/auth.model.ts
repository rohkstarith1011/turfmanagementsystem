export interface AuthRequestDTO {
  email: string;
  password?: string;
}

export interface AuthResponseDTO {
  token: string;
  userId: string;
  name: string;
  email: string;
  roles: string[];
}
