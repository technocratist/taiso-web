import { post } from "../api/request";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  email: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface RegisterResponse {
  userId: number;
  email: string;
}

const login = async (payload: LoginRequest): Promise<LoginResponse> => {
  return await post("/auth/login", payload);
};

const register = async (
  payload: RegisterRequest
): Promise<RegisterResponse> => {
  return await post("/auth/register", payload);
};

const logout = async (): Promise<void> => {
  return await post("/auth/logout");
};

export default {
  login,
  register,
  logout,
};
