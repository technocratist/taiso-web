import { useEffect, useState } from "react";
import routeService, { RouteListResponse } from "../services/routeService";
import { Link } from "react-router";

function RouteList() {
  const [isLoading, setIsLoading] = useState(true);
  const [routeList, setRouteList] = useState<RouteListResponse[]>([]);

  useEffect(() => {
    const fetchRouteList = async () => {
      const data = await routeService.getRouteList();
      setRouteList(data.content);
      setIsLoading(false);
    };
    fetchRouteList();
  }, []);

  console.log(routeList);

  if (isLoading) {
    return [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((item) => (
      <div key={item} className="skeleton w-60 h-60"></div>
    ));
  }

  return (
    <div className="flex gap-4">
      {routeList.map((route) => (
        <Link to={`/route/${route.routeId}`} key={route.routeId}>
          <div className="card bg-base-100 w-60 shadow-xl">
            <figure>
              <img
                src={route.routeImgId}
                alt={route.routeName}
                className="w-full h-full"
              />
            </figure>
            <div className="card-body">
              <h2 className="card-title">{route.routeName}</h2>
              <p>{route.distance}</p>
              <p>{route.altitude}</p>
              <p>{route.roadType}</p>
              <p>{route.createdAt}</p>
            </div>
          </div>
        </Link>
      ))}
    </div>
  );
}

export default RouteList;
