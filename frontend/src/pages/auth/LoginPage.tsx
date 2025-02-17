import LoginForm from "../../components/LoginForm";
import authService from "../../services/authService";

function LoginPage() {
  const handleAuthTest = async () => {
    const response = await authService.authTest();
    console.log(response);
  };
  return (
    <div>
      <LoginForm />
      <button onClick={handleAuthTest}>테스트</button>
    </div>
  );
}

export default LoginPage;
