// src/api/request.ts
import axiosClient from "./axiosClient";

export interface ApiResponse<T = any> {
  data: T;
  // status, message 등 추가 필드가 있을 경우 여기서 확장 가능
}

// GET: URL과 선택적 쿼리 파라미터
export const get = async <T>(url: string, params?: any): Promise<T> => {
  const response = await axiosClient.get<T>(url, { params });
  return response.data;
};

// POST: URL과 body 데이터를 전달
export const post = async <T>(url: string, data?: any): Promise<T> => {
  const response = await axiosClient.post<T>(url, data);
  console.log(response);
  return response.data;
};

export const put = async <T>(url: string, data?: any): Promise<T> => {
  const response = await axiosClient.put<T>(url, data);
  return response.data;
};

export const patch = async <T>(url: string, data?: any): Promise<T> => {
  const response = await axiosClient.patch<T>(url, data);
  return response.data;
};

export const del = async <T>(url: string, params?: any): Promise<T> => {
  const response = await axiosClient.delete<T>(url, { params });
  return response.data;
};
