import { Link } from "react-router-dom";

function LandingPage() {
  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=31c2e0a2025d6447f459a1c9e4f9ae00&redirect_uri=http://localhost:3000/auth/login/kakao&response_type=code`;

  const handleKakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <div className="text-4xl font-bold mb-24">taiso - 자전거 함 타이소</div>

      <Link to="/login">
        <div className="btn btn-primary no-animation btn-wide">로그인 하기</div>
      </Link>
      <Link to="/register">
        <div className="btn btn-primary no-animation btn-wide mt-10">
          회원가입 하기
        </div>
      </Link>

      <div
        onClick={handleKakaoLogin}
        className="btn bg-yellow-400 no-animation btn-wide mt-10 hover:bg-yellow-500"
      >
        카카오 로그인하기
      </div>
    </div>
  );
}

export default LandingPage;
