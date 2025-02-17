// src/store/authStore.ts
import { create } from "zustand";
import { persist } from "zustand/middleware";

export interface User {
  email: string;
  // 필요한 추가 필드
}

interface AuthState {
  isAuthenticated: boolean;
  user: User | null;
  // 상태 업데이트 액션들
  setUser: (user: User | null) => void;
  logout: () => void;
}

export const useAuthStore = create(
  persist<AuthState>(
    (set) => ({
      isAuthenticated: false,
      user: null,
      setUser: (user: User | null) =>
        set(() => ({ user, isAuthenticated: !!user })),
      logout: () => set({ user: null, isAuthenticated: false }),
    }),
    {
      name: "auth-storage", // localStorage에 저장될 키 값
      // storage 옵션을 별도로 지정하지 않으면 기본적으로 localStorage가 사용됩니다.
    }
  )
);
