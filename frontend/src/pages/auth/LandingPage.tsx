import { Link } from "react-router";

function LandingPage() {
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
      <div className="btn bg-yellow-400 no-animation btn-wide mt-10 hover:bg-yellow-500">
        카카오 로그인하기
      </div>
    </div>
  );
}

export default LandingPage;
