import { Navigate, Outlet } from "react-router";
import { useAuthStore } from "./stores/useAuthStore";

function AuthRoute() {
  const { isAuthenticated } = useAuthStore();
  // 인증되어있는 경우 메인 페이지로 이동
  if (isAuthenticated) {
    return <Navigate to="/" />;
  }
  return <Outlet />;
}

export default AuthRoute;
