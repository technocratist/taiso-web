import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
// 데이터 불러오기
import lightningService, {
  Lightning,
  LightningListResponse,
} from "../services/lightningService";
// 이미지 요청 관련
import ImageWithSkeleton from "./ImageWithSkeleton";

function LightningList() {
  const PAGE_SIZE = 8;
  // 로딩값
  const [isLoading, setIsLoading] = useState(false);
  const [lightningList, setLightningList] = useState<Lightning[]>([]);
  // 페이지 관련 변경 값
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

  // 비동기 데이터 불러오기
  const fetchLightningList = async () => {
    try {
      setIsLoading(true);
      const data = await lightningService.getLightningList(
        page,
        PAGE_SIZE,
        sort
      );
      setIsLastPage(data.last);
      setLightningList((prev) =>
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
    fetchLightningList();
  }, [page, sort]);

  console.log(lightningList);

  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  // 예: 필터 변경 시 페이지를 초기화하고 기존 목록을 지워 새로 불러옴 (필요한 경우)
  const handleFilterChange = (newSort: string) => {
    setSort(newSort);
    setPage(0);
  };

  // 날짜 포멧팅
  const formatDate = (date: string | number | Date) => {
    const options: Intl.DateTimeFormatOptions = {
      month: "long",
      day: "numeric",
      hour: "numeric",
      minute: "numeric",
      hour12: false, // 24시간 포맷
    };

    return new Date(date)
      .toLocaleString("ko-KR", options)
      .replace("오전", "")
      .replace("오후", "");
  };

  const renderStatusButton = (status: string) => {
    switch (status) {
      case "모집":
        return (
          <button className="btn btn-outline btn-success sm:w-[150px] no-animation">
            참가
          </button>
        );
      case "마감":
        return (
          <button className="btn btn-outline btn-error sm:w-[150px] no-animation">
            마감
          </button>
        );
      case "종료":
      default:
        return (
          <button className="btn sm:w-[150px] no-animation" disabled>
            종료
          </button>
        );
    }
  };

  if (isLoading) {
    return (
      <div className="flex w-full justify-center mt-4">
        <div className="loading loading-dots loading-lg"></div>
      </div>
    );
  }
  // 선택용 날짜/요일 출력

  return (
    <div className="flex flex-col">
      <div className="flex flex-wrap justify-center gap-2 mt-4">
        {lightningList.map((lightning) => (
          <div key={lightning.lightningId} className="w-[90%]">
            <Link to={`/lightning/${lightning.lightningId}`}>
              <div className="bg-base-100 w-full flex items-center">
                <figure className="size-40 flex items-center justify-center ml-4 relative">
                  <ImageWithSkeleton
                    src={lightning.routeImgId}
                    alt={lightning.title}
                  />
                </figure>
                <div className="flex flex-col p-2 ml-6">
                  <div className="flex flex-col ">
                    <div className="text-xs text-gray-500">
                      {formatDate(lightning.eventDate)} ({lightning.duration}분)
                    </div>
                    <div className="text-lg font-semibold">
                      {lightning.title}
                    </div>
                    <div className="text-sm text-gray-500 flex items-center gap-1">
                      <svg
                        data-slot="icon"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                        xmlns="http://www.w3.org/2000/svg"
                        aria-hidden="true"
                        className="size-4"
                      >
                        <path
                          clip-rule="evenodd"
                          fill-rule="evenodd"
                          d="m9.69 18.933.003.001C9.89 19.02 10 19 10 19s.11.02.308-.066l.002-.001.006-.003.018-.008a5.741 5.741 0 0 0 .281-.14c.186-.096.446-.24.757-.433.62-.384 1.445-.966 2.274-1.765C15.302 14.988 17 12.493 17 9A7 7 0 1 0 3 9c0 3.492 1.698 5.988 3.355 7.584a13.731 13.731 0 0 0 2.273 1.765 11.842 11.842 0 0 0 .976.544l.062.029.018.008.006.003ZM10 11.25a2.25 2.25 0 1 0 0-4.5 2.25 2.25 0 0 0 0 4.5Z"
                        ></path>
                      </svg>
                      {lightning.address}
                    </div>
                    <div className="text-sm text-gray-500 flex items-center gap-1 ">
                      <svg
                        data-slot="icon"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                        xmlns="http://www.w3.org/2000/svg"
                        aria-hidden="true"
                        className="size-4"
                      >
                        <path d="M10 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM3.465 14.493a1.23 1.23 0 0 0 .41 1.412A9.957 9.957 0 0 0 10 18c2.31 0 4.438-.784 6.131-2.1.43-.333.604-.903.408-1.41a7.002 7.002 0 0 0-13.074.003Z"></path>
                      </svg>
                      {lightning.capacity}/{lightning.capacity}명
                    </div>
                    <div className="flex flex-wrap gap-1 mt-2">
                      <div className="badge badge-primary badge-outline">
                        {lightning.gender}
                      </div>
                      <div className="badge badge-primary badge-outline">
                        {lightning.level}
                      </div>
                      <div className="badge badge-primary badge-outline">
                        {lightning.bikeType}
                      </div>
                      {lightning.tags.map((tag, index) => (
                        <div
                          key={index}
                          className="badge badge-primary badge-outline"
                        >
                          {tag}
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="p-4 mt-auto ml-auto flex items-center justify-center">
                  <Link
                    to={`/lightning/${lightning.lightningId}`}
                    className="group"
                  >
                    {renderStatusButton(lightning.status)}
                  </Link>
                </div>
              </div>
            </Link>
            <div className="divider w-full -my-2"></div>
          </div>
        ))}
      </div>
      {isLoading && (
        <div className="flex w-full justify-center mt-4">
          <div className="loading loading-dots loading-lg"></div>
        </div>
      )}
      {!isLoading && hasMore && (
        <button
          className="btn btn-wide mt-4 mx-auto no-animation"
          onClick={handleLoadMore}
        >
          더보기
        </button>
      )}
      {isLastPage && (
        <div className="text-center text-gray-400 mt-4 mb-10">
          마지막 페이지입니다.
        </div>
      )}
    </div>
  );
}

export default LightningList;
