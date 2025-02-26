import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
// 데이터 불러오기
import lightningService, { LightningListResponse } from "../services/lightningService";
// 이미지 요청 관련
import ImageWithSkeleton from "./ImageWithSkeleton";

function LightningList() {
  const PAGE_SIZE = 8;
  // 로딩값
  const [isLoading, setIsLoading] = useState(false);
  const [lightningList, setLightningList] = useState<LightningListResponse[]>([]);
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
          // 서비스 설정 필요
        setIsLoading(true);
        const data = await lightningService.getLightningList(
          page,
          PAGE_SIZE,
          sort
        );
        setIsLastPage(data.last);
        // 페이지 0일 때는 초기화, 그렇지 않으면 기존 데이터에 추가
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

  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  // 예: 필터 변경 시 페이지를 초기화하고 기존 목록을 지워 새로 불러옴 (필요한 경우)
  const handleFilterChange = (newSort: string) => {
    setSort(newSort);
    setPage(0);
  };

  // 날짜 포멧팅
  const formatDate = (date) => {
    const options = {
      month: 'long',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
      hour12: false, // 24시간 포맷
    };

    return new Date(date).toLocaleString('ko-KR', options).replace('오전', '').replace('오후', '');
  };

  // 선택용 날짜/요일 출력

  return (

    <div className="flex flex-col ">
        <div className="flex flex-wrap justify-center gap-2">
          {lightningList.map((lightning) => (
            <div key={lightning.lightningId} className="card card-side bg-base-100 shadow-xl w-full">
                 <ImageWithSkeleton src={lightning.routeImgId} alt={lightning.title} />
              <div className="card-body">
                <div>
                  <p>{formatDate(lightning.eventDate)} ({lightning.duration}분)</p>
                  <h2 className="card-title">{lightning.title}</h2>
                  <p>{lightning.address}</p>
                  <p>{lightning.capacity}/{lightning.capacity}명</p>
                  <div>
                    <div className="badge badge-primary badge-outline">{lightning.gender}</div>
                    <div className="badge badge-primary badge-outline">{lightning.level}</div>
                    <div className="badge badge-primary badge-outline">{lightning.bikeType}</div>
                  </div>
                </div>
               <div className="card-actions justify-end">
                   <Link
                       to={`/lightning/${lightning.lightningId}`}
                       key={lightning.lightningId}
                       className="group"
                     >
                     {(() => {
                       switch (lightning.status) {
                         case '모집':
                           return <button className="btn btn-outline btn-success w-[200px]">번개 참가</button>;
                         case '마감':
                           return <button className="btn btn-outline btn-error w-[200px]">번개 마감</button>;
                         case '종료':
                           return <button className="btn w-[200px]" disabled>번개 종료</button>;
                         default:
                           return <button className="btn w-[200px]" disabled>번개 종료</button>;
                       }
                     })()}
                   </Link>
               </div>
              </div>
            </div>
          ))}

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
    </div>
  );
}

export default LightningList;
