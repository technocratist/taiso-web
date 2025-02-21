import { useEffect, useState } from "react";
import routeService, { RouteListResponse } from "../services/routeService";
import { Link, useNavigate } from "react-router-dom";
import ImageWithSkeleton from "./ImageWithSkeleton";

function RouteList() {
  const PAGE_SIZE = 8;
  const [isLoading, setIsLoading] = useState(false);
  const [routeList, setRouteList] = useState<RouteListResponse[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  // 필터 상태들 (필요에 따라 업데이트)
  const [sort, setSort] = useState("");
  const [isLastPage, setIsLastPage] = useState(false);
  const [distanceType, setDistanceType] = useState("");
  const [altitudeType, setAltitudeType] = useState("");
  const [roadType, setRoadType] = useState("");
  const [tag, setTag] = useState([]);

  const navigate = useNavigate();

  const fetchRouteList = async () => {
    try {
      setIsLoading(true);
      const data = await routeService.getRouteList(
        page,
        PAGE_SIZE,
        sort,
        distanceType,
        altitudeType,
        roadType,
        tag
      );
      setIsLastPage(data.last);
      // 페이지 0일 때는 초기화, 그렇지 않으면 기존 데이터에 추가
      setRouteList((prev) =>
        page === 0 ? data.content : [...prev, ...data.content]
      );
      // 반환된 항목 수가 PAGE_SIZE보다 작으면 더 이상 불러올 데이터 없음
      if (data.content.length < PAGE_SIZE) {
        setHasMore(false);
      } else {
        setHasMore(true);
      }
    } catch (err) {
      navigate("/error");
    } finally {
      setIsLoading(false);
    }
  };

  // 페이지 번호나 필터가 변경될 때마다 데이터를 다시 불러옴
  useEffect(() => {
    fetchRouteList();
  }, [page, sort, distanceType, altitudeType, roadType, tag]);

  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  // 예: 필터 변경 시 페이지를 초기화하고 기존 목록을 지워 새로 불러옴 (필요한 경우)
  const handleFilterChange = (newSort: string) => {
    setSort(newSort);
    setPage(0);
    setRouteList([]);
  };

  return (
    <div className="flex flex-col ">
      <div className="flex justify-start mb-4 gap-2">
        <div className="badge badge-primary badge-outline badge-lg text-sm cursor-pointer hover:bg-primary hover:text-white">
          랭킹순
        </div>
        <div className="badge badge-primary badge-outline badge-lg text-sm cursor-pointer hover:bg-primary hover:text-white">
          최신순
        </div>
        <div className="badge badge-primary badge-outline badge-lg text-sm cursor-pointer hover:bg-primary hover:text-white">
          태그
        </div>
      </div>
      <div className="flex flex-wrap justify-center gap-6">
        {routeList.map((route) => (
          <Link to={`/route/${route.routeId}`} key={route.routeId}>
            <div className="card card-compact bg-base-100 w-56 shadow-xl">
              <figure className="relative overflow-hidden aspect-[4/3]">
                <ImageWithSkeleton
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
      {isLoading && (
        <div className="flex w-full justify-center mt-4">
          <div className="loading loading-dots loading-lg"></div>
        </div>
      )}
      {!isLoading && hasMore && (
        <button
          className="btn btn-primary btn-wide mt-4 mx-auto"
          onClick={handleLoadMore}
        >
          더보기
        </button>
      )}
      {isLastPage && (
        <div className="text-center text-gray-400 mt-4">
          마지막 페이지입니다.
        </div>
      )}
    </div>
  );
}

export default RouteList;
