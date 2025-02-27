import { useNavigate, useParams } from "react-router";
import lightningService, { LightningDetailGetResponse } from "../../services/lightningService";
import { useEffect, useState } from "react";
import { useAuthStore } from "../../stores/useAuthStore";

function LightningDetailPage() {
   const { lightningId } = useParams();
   const [lightningDetail, setLightningDetail] = useState<LightningDetailGetResponse | null>(null);

   const [isLoading, setIsLoading] = useState(true);
   const { user } = useAuthStore();
   const navigate = useNavigate();

    useEffect(() => {
    const fetchLightningDetail = async () => {
     setIsLoading(true);
     const lightningDetailData = await lightningService.getLightningDetail(
       Number(lightningId)
     );
     setLightningDetail(lightningDetailData);
     setIsLoading(false);
    };
    fetchLightningDetail();
     }, [lightningId]);


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

    return (
        <div className="flex flex-col mt-2 gap-2 md:w-full w-[90%]">
          <div className="flex items-center gap-1">
            <h2 className="text-4xl font-bold">{lightningDetail?.title}</h2>
               {lightningDetail?.lightningTag.map((tag, index) => (
              <div key={index} className="badge badge-outline badge-primary">
                {tag}
              </div>
              ))}
              <div className="badge badge-outline badge-primary">
                {lightningDetail?.gender}
              </div>
              <div className="badge badge-outline badge-primary">
                {lightningDetail?.level}
              </div>
              <div className="badge badge-outline badge-primary">
                {lightningDetail?.bikeType}
              </div>
              <div className="badge badge-outline badge-primary">
                {lightningDetail?.region}
              </div>
              <div className="badge badge-outline badge-primary">
                {lightningDetail?.recruitType}
              </div>

            <div className="avatar">
              <div className="w-24 rounded-full">
              </div>
                <span>{lightningDetail?.description}</span>
            </div>
          </div>

          <div className="flex items-center gap-1">
          <h2>주의 사항</h2>
          <p></p>
          </div>

          <div className="flex items-center gap-1 hover:bg-base-200 p-1 rounded-md w-fit">
            <div>
                <span>{formatDate(lightningDetail?.eventDate)}</span>
                <h3>{lightningDetail?.address}</h3>
            </div>
            <svg
              data-slot="icon"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
              className="size-6"
            >
              <path d="M3 3.5A1.5 1.5 0 0 1 4.5 2h6.879a1.5 1.5 0 0 1 1.06.44l4.122 4.12A1.5 1.5 0 0 1 17 7.622V16.5a1.5 1.5 0 0 1-1.5 1.5h-11A1.5 1.5 0 0 1 3 16.5v-13Z" />
            </svg>
            <div className="text-sm link">{lightningDetail?.address}</div>
          </div>


          <div className="flex items-center justify-center gap-1">
            <span>참가 마감 인원까지 {lightningDetail?.capacity}명 남았습니다.</span>
            {lightningDetail?.status ? (
              (() => {
                switch (lightningDetail.status) {
                  case '모집':
                    return <button className="btn btn-outline btn-success w-[200px]">번개 참가</button>;
                  case '마감':
                    return <button className="btn btn-outline btn-error w-[200px]">번개 마감</button>;
                  case '종료':
                    return <button className="btn w-[200px]" disabled>번개 종료</button>;
                  default:
                    return <button className="btn w-[200px]" disabled>번개 종료</button>;
                }
              })()
            ) : (
              <button className="btn w-[200px]" disabled>상태 불명</button> // 예시로, 상태가 없으면 "상태 불명" 버튼 표시
            )}
          </div>

        </div>
    );
}


export default LightningDetailPage;