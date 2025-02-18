import { get } from "../api/request";
import { post } from "../api/request";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  userEmail: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface RegisterResponse {
  userId: number;
  email: string;
}

export interface AuthTestResponse {
  message: string;
  timestamp: string;
  status: string;
}

const login = async (payload: LoginRequest): Promise<LoginResponse> => {
  const response: LoginResponse = await post("/auth/login", payload);
  console.log(response);
  return response;
};

const register = async (
  payload: RegisterRequest
): Promise<RegisterResponse> => {
  return await post("/auth/register", payload);
};

const logout = async (): Promise<void> => {
  return await post("/auth/logout", {});
};

const authTest = async (): Promise<AuthTestResponse> => {
  return await get("/test");
};

export default {
  login,
  register,
  logout,
  authTest,
};
