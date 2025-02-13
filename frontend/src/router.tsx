import { createBrowserRouter } from "react-router-dom";
import Root from "./root";
import TestPage from "./pages/TestPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        path: "/",
        element: <TestPage />,
      },
    ],
  },
]);

export default router;
