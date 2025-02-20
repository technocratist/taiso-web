import { Outlet } from "react-router";

function Root() {
  return (
    <div className="flex flex-col items-center justify-center max-w-screen-md mx-auto">
      <Outlet />
    </div>
  );
}

export default Root;
