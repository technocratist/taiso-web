import { createBrowserRouter } from "react-router-dom";
import Root from "./root";
import MainPage from "./pages/MainPage";
import ProtectedRoute from "./ProtectedRoute";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import LandingPage from "./pages/auth/LandingPage";
import AuthRoute from "./AuthRoute";
import RoutePostPage from "./pages/route/RoutePostPage";
import OAuthCallback from "./components/OAuthCallback";
import RoutePage from "./pages/RoutePage";
import ClubPage from "./pages/ClubPage";
import RouteDetailPage from "./pages/route/RouteDetailPage";
import NotFoundErrorPage from "./pages/error/NotFoundErrorPage";
import LightningPage from "./pages/LightningPage";
import LightningPostPage from "./pages/lightning/LightningPostPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      // 인증 없이 접근 가능한 페이지들
      {
        element: <AuthRoute />,
        children: [
          { path: "", element: <MainPage /> },
          { path: "landing", element: <LandingPage /> },
          { path: "login", element: <LoginPage /> },
          { path: "register", element: <RegisterPage /> },
          { path: "oauth/callback", element: <OAuthCallback /> },
          {
            path: "lightning",
            children: [
              { path: "", element: <LightningPage /> },
              // 인증 필요한 post 라우트는 아래 ProtectedRoute에서 다룸
            ],
          },
          {
            path: "route",
            children: [
              { path: "", element: <RoutePage /> },
              { path: ":routeId", element: <RouteDetailPage /> },
            ],
          },
          {
            path: "club",
            children: [{ path: "", element: <ClubPage /> }],
          },
        ],
      },
      // 인증 후에만 접근 가능한 post 관련 페이지들
      {
        element: <ProtectedRoute />,
        children: [
          {
            path: "lightning",
            children: [{ path: "post", element: <LightningPostPage /> }],
          },
          {
            path: "route",
            children: [{ path: "post", element: <RoutePostPage /> }],
          },
        ],
      },
    ],
  },
  {
    path: "*",
    element: <NotFoundErrorPage />,
  },
]);

export default router;
