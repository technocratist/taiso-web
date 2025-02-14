// src/api/axiosClient.ts
import axios, { AxiosRequestConfig, AxiosResponse, AxiosError } from "axios";

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL as string,
  timeout: 10000,
  withCredentials: true, // HttpOnly 쿠키 사용 시 필수!
});

// 요청 인터셉터: HttpOnly 쿠키를 사용하는 경우, 별도의 토큰 추가 로직이 필요 없음
axiosClient.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 추가적인 config 설정이 필요한 경우 이곳에서 처리
    return config;
  },
  (error: AxiosError) => Promise.reject(error)
);

// 응답 인터셉터: 401 에러 발생 시 로그인 상태 만료 처리
axiosClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      console.error("인증 오류: 로그인 상태가 만료되었습니다.");
      // 서버에서 HttpOnly 쿠키를 삭제했을 수 있으므로, 로그인 페이지로 리다이렉션하거나 별도 로그아웃 로직 실행
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default axiosClient;
