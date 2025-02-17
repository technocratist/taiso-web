import { useState } from "react";
import authService, {
  RegisterRequest,
  RegisterResponse,
} from "../services/authService";

function RegisterForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const payload: RegisterRequest = {
      email,
      password,
    };
    try {
      const response: RegisterResponse = await authService.register(payload);
    } catch (error) {
      setError("회원가입에 실패했습니다.");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        className="border border-gray-300 rounded-md p-2"
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <input
        className="border border-gray-300 rounded-md p-2"
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <input
        className="border border-gray-300 rounded-md p-2"
        type="password"
        placeholder="Password Confirm"
        value={passwordConfirm}
        onChange={(e) => setPasswordConfirm(e.target.value)}
      />
      <button className="btn btn-primary no-animation btn-wide">
        회원가입
      </button>
      {error && <div className="text-red-500">{error}</div>}
    </form>
  );
}

export default RegisterForm;
