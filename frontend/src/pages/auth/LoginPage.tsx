import { Link } from "react-router";
import LoginForm from "../../components/LoginForm";

function LoginPage() {
  return (
    <div className="flex flex-col items-center justify-center mt-16">
      <div className="text-2xl font-bold mb-16">이메일로 로그인 하기</div>
      <LoginForm />
      <div className="text-sm divider mt-8 mb-6">아직 아이디가 없으신가요?</div>
      <Link to="/auth/register">
        <div className="btn no-animation w-[20rem] font-bold">
          회원가입 하기
        </div>
      </Link>
    </div>
  );
}

export default LoginPage;
