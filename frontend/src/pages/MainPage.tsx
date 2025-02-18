import authService from "../services/authService";
import { useAuthStore } from "../stores/useAuthStore";

function MainPage() {
  const { logout } = useAuthStore();

  const handleLogout = () => {
    authService.logout();
    logout();
  };

  return (
    <div>
      <div>MainPage</div>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
}

export default MainPage;
