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
      <div className="flex w-full justify-center">
        <div className="loading loading-dots loading-lg"></div>
      </div>
    );
  }

  return (
    <div className="flex flex-wrap justify-center gap-6 ">
      {routeList.map((route) => (
        <Link to={`/route/${route.routeId}`} key={route.routeId}>
          <div className="card card-compact bg-base-100 w-56 shadow-xl">
            <figure className=" relative overflow-hidden">
              <img
                src={route.routeImgId}
                alt={route.routeName}
                className="object-cover w-full h-full"
              />
            </figure>
            <div className="card-body">
              <h2 className="card-title -mt-1">{route.routeName}</h2>
              <div className="flex justify-between text-sm text-gray-600 -mt-1">
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
