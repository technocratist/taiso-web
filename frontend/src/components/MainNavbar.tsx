import { NavLink } from "react-router-dom";

function MainNavbar() {
  return (
    <div className="flex flex-col items-center justify-center">
      <div className="flex gap-6 no-animation mt-4">
        <NavLink
          to="/"
          end
          className={({ isActive }) =>
            `btn ${
              isActive ? "btn-primary" : "btn-outline btn-primary"
            } w-32 rounded-full btn-sm`
          }
        >
          번개
        </NavLink>
        <NavLink
          to="/club"
          end
          className={({ isActive }) =>
            `btn ${
              isActive ? "btn-primary" : "btn-outline btn-primary"
            } w-32 rounded-full btn-sm`
          }
        >
          클럽
        </NavLink>
        <NavLink
          to="/route"
          end
          className={({ isActive }) =>
            `btn ${
              isActive ? "btn-primary" : "btn-outline btn-primary"
            } w-32 rounded-full btn-sm`
          }
        >
          루트
        </NavLink>
      </div>
    </div>
  );
}

export default MainNavbar;
