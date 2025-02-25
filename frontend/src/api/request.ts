import axiosClient from "./axiosClient";

export interface ApiResponse<T = unknown> {
  data: T;
  // 필요시 status, message 등 추가 필드 확장
}

// Define a generic type for query parameters, defaulting to a record of unknown values.
export const get = async <T, P = Record<string, unknown>>(
  url: string,
  params?: P,
  signal?: AbortSignal
): Promise<T> => {
  const response = await axiosClient.get<T>(url, { params, signal });
  return response.data;
};

// For POST, PUT, PATCH, we define a generic type for the request data.
export const post = async <T, D = unknown>(
  url: string,
  data?: D
): Promise<T> => {
  const response = await axiosClient.post<T>(url, data);
  return response.data;
};

export const put = async <T, D = unknown>(
  url: string,
  data?: D
): Promise<T> => {
  const response = await axiosClient.put<T>(url, data);
  return response.data;
};

export const patch = async <T, D = unknown>(
  url: string,
  data?: D
): Promise<T> => {
  const response = await axiosClient.patch<T>(url, data);
  return response.data;
};

export const del = async <T>(url: string): Promise<T> => {
  const response = await axiosClient.delete<T>(url);
  return response.data;
};
