import { useState } from "react";
import AltitudeChart from "./AltitudeChart";
import KakaoMapRoute from "./KakaoMap";

// Import the actual RoutePoint type instead of defining our own
// Look for this type in your models or services folders
// For example:
// import { RoutePoint } from "../types/routeTypes";

const RouteViewer = ({ routePoints }: { routePoints: any }) => {
  // Use the imported type, or use 'any' temporarily until you locate the proper type
  const [hoveredPoint, setHoveredPoint] = useState<any | null>(null);

  return (
    <div>
      <KakaoMapRoute
        routePoints={routePoints}
        selectedPoint={hoveredPoint} // 선택(hover)된 포인트 전달
      />
      <AltitudeChart
        routePoints={routePoints}
        onPointHover={setHoveredPoint} // 마우스 hover 시 포인트 업데이트
      />
    </div>
  );
};

export default RouteViewer;
