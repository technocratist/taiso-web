import LoginForm from "../../components/LoginForm";

function LoginPage() {
  return (
    <div className="flex flex-col items-center justify-center mt-12">
      <div className="text-4xl font-bold mb-8">Taiso</div>
      <LoginForm />
    </div>
  );
}

export default LoginPage;
