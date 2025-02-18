import { Link } from "react-router";
import MainNavbar from "../components/MainNavbar";

function RoutePage() {
  return (
    <div>
      <MainNavbar />
      route
      <Link to="/route/post">루트 등록하기 버튼</Link>
    </div>
  );
}

export default RoutePage;
