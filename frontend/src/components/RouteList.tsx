import { useEffect, useState } from "react";
import routeService, { RouteListResponse } from "../services/routeService";
import { Link, useNavigate } from "react-router-dom";

function RouteList() {
  const [isLoading, setIsLoading] = useState(true);
  const [routeList, setRouteList] = useState<RouteListResponse[]>([]);

  const navigate = useNavigate();
  const fetchRouteList = async () => {
    try {
      setIsLoading(true);
      const data = await routeService.getRouteList();
      setRouteList(data.content);
    } catch (err) {
      navigate("/error");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchRouteList();
  }, []);

  if (isLoading) {
    // 스켈레톤 카드 UI
    return (
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {Array.from({ length: 6 }).map((_, index) => (
          <div key={index} className="animate-pulse bg-gray-200 rounded-lg p-4">
            <div className="h-40 bg-gray-300 rounded mb-4"></div>
            <div className="h-6 bg-gray-300 rounded mb-2 w-3/4"></div>
            <div className="h-4 bg-gray-300 rounded mb-2 w-1/2"></div>
            <div className="h-4 bg-gray-300 rounded w-1/3"></div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="flex flex-wrap justify-center gap-6">
      {routeList.map((route) => (
        <Link to={`/route/${route.routeId}`} key={route.routeId}>
          <div className="card bg-base-100 w-72 shadow-xl">
            <figure className=" relative h-48 overflow-hidden">
              <img
                src={route.routeImgId}
                alt={route.routeName}
                className="object-cover w-full h-full"
              />
            </figure>
            <div className="card-body">
              <h2 className="card-title">{route.routeName}</h2>
              <div className="flex justify-between text-sm text-gray-600">
                <span>{route.distance} km</span>
                <span>{route.altitude} m</span>
              </div>
              <p className="text-xs">{route.roadType}</p>
              <p className="text-xs text-gray-400">
                {new Date(route.createdAt).toLocaleDateString()}
              </p>
            </div>
          </div>
        </Link>
      ))}
    </div>
  );
}

export default RouteList;
