// src/pages/OAuthCallback.tsx
import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import authService from "../services/authService";

const OAuthCallback: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const query = new URLSearchParams(location.search);
    const code = query.get("code");

    // state 값 검증 (CSRF 방지를 위해 실제 환경에서는 비교 로직 필요)
    if (code) {
      authService.kakaoLogin(code).then((token) => {
        localStorage.setItem("jwtToken", token);
        navigate("/");
      });
    }
  }, [location, navigate]);

  return (
    <div>
      <p>로그인 중입니다...</p>
    </div>
  );
};

export default OAuthCallback;
