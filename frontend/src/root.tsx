import { Outlet } from "react-router";
import Navbar from "./components/Navbar";

function Root() {
  return (
    <div className="flex flex-col items-center justify-center max-w-screen-lg mx-auto">
      <Navbar />
      <Outlet />
    </div>
  );
}

export default Root;
