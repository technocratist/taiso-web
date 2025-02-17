import { createBrowserRouter } from "react-router-dom";
import Root from "./root";
import TestPage from "./pages/TestPage";
import MainPage from "./pages/MainPage";
import ProtectedRoute from "./ProtectedRoute";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import LandingPage from "./pages/auth/LandingPage";
import AuthRoute from "./AuthRoute";
import RouteListPage from "./pages/route/RouteListPage";
import RoutePostPage from "./pages/route/RoutePostPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      // 로그인 페이지는 공용 레이아웃에 포함하지만 보호되지 않음
      {
        element: <AuthRoute />,
        children: [
          { path: "landing", element: <LandingPage /> },
          { path: "login", element: <LoginPage /> },
          { path: "register", element: <RegisterPage /> },
        ],
      },
      // 로그인 후에만 접근 가능한 페이지들
      {
        element: <ProtectedRoute />,
        children: [
          { index: true, element: <MainPage /> },
          { path: "test", element: <TestPage /> },
          {
            path: "route",
            children: [
              { path: "", element: <RouteListPage /> },
              { path: "post", element: <RoutePostPage /> },
            ],
          },
          // 추가적인 인증이 필요한 페이지들을 여기에 추가
        ],
      },
    ],
  },
]);

export default router;
