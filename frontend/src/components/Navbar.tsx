import { useState } from "react";
import authService from "../services/authService";
import { useAuthStore } from "../stores/useAuthStore";

function Navbar() {
  const { logout } = useAuthStore();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const handleLogout = () => {
    authService.logout();
    logout();
  };

  return (
    <>
      <div className="navbar">
        <div className="flex-1">
          <a className="btn btn-ghost text-xl no-animation">taiso</a>
        </div>
        <div className="flex-none gap-2">
          <ul className="menu menu-horizontal px-1">
            <li>
              <div onClick={handleLogout}>logout</div>
            </li>
          </ul>
          <div
            onClick={() => setIsSidebarOpen(true)}
            className="btn btn-ghost btn-circle avatar cursor-pointer"
          >
            <div className="w-10 rounded-full">
              <img
                alt="Avatar"
                src="https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.webp"
              />
            </div>
          </div>
        </div>
      </div>

      <div
        className={`fixed top-0 right-0 z-50 h-full w-64 bg-base-100 shadow-lg transition-transform duration-300 ${
          isSidebarOpen ? "translate-x-0" : "translate-x-full"
        }`}
      >
        <div className="p-4">
          <h2 className="text-xl font-bold mb-4">Menu</h2>
          <ul className="menu">
            <li>
              <a>Profile</a>
            </li>
            <li>
              <a>Settings</a>
            </li>
            <li>
              <a onClick={handleLogout}>Logout</a>
            </li>
          </ul>
          <button
            onClick={() => setIsSidebarOpen(false)}
            className="mt-4 btn btn-sm"
          >
            Close
          </button>
        </div>
      </div>
    </>
  );
}

export default Navbar;
