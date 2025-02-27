import { Link } from "react-router-dom";

function LandingPage() {
  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${
    import.meta.env.VITE_KAKAO_CLIENT_ID
  }&redirect_uri=http://localhost:3000/oauth/callback&response_type=code`;

  const handleKakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  return (
    <div className="flex flex-col items-center justify-center mt-12">
      <div className="text-4xl font-bold mb-4">Taiso</div>
      <div className="text-xl mb-24 font-light">
        로그인하고 번개에 참여해보세요!
      </div>

      <div className="flex flex-col gap-4">
        <div
          onClick={handleKakaoLogin}
          className="btn btn-wide bg-yellow-300 no-animation hover:bg-yellow-400 font-bold"
        >
          카카오 로그인하기
        </div>
        <Link to="/auth/login">
          <div className="btn no-animation btn-wide font-bold">로그인 하기</div>
        </Link>
        <div>아직 아이디가 없으신가요?</div>
        <Link to="/auth/register">
          <div className="btn no-animation btn-wide font-bold">
            회원가입 하기
          </div>
        </Link>
      </div>
    </div>
  );
}

export default LandingPage;
