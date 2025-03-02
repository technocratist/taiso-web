import RegisterForm from "../../components/RegisterForm";

function RegisterPage() {
  return (
    <div className="flex flex-col items-center justify-center mt-16">
      <div className="text-2xl font-bold mb-12">회원가입 하기</div>
      <RegisterForm />
    </div>
  );
}

export default RegisterPage;
