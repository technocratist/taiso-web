import { useNavigate, useParams } from "react-router";
import lightningService, { LightningDetailGetResponse } from "../../services/routeService";
import { useEffect, useState } from "react";
import { useAuthStore } from "../../stores/useAuthStore";

function LightningDetailPage() {
   const { ligtningId } = useParams();
   const [ligtningDetail, setLigtningDetail] = useState<LightningDetailGetResponse | null>
    null
   );
   const [isLoading, setIsLoading] = useState(true);
   const [likePending, setLikePending] = useState(false);
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
          <div>
          //
            <ImageWithSkeleton src={ligtningDetail.routeImgId} alt={ligtningDetail.title} />
          </div>

          <div className="flex items-center gap-1">
            <h2 className="text-4xl font-bold">{ligtningDetail?.title}</h2>
               {ligtningDetail.?.tag.map((tag, index) => (
              <div key={index} className="badge badge-outline badge-primary">
                {tag}
              </div>
              ))}
              <div className="badge badge-outline badge-primary">
                {ligtningDetail?.gender}
              </div>
              <div className="badge badge-outline badge-primary">
                {ligtningDetail?.level}
              </div>
              <div className="badge badge-outline badge-primary">
                {ligtningDetail?.bikeType}
              </div>
              <div className="badge badge-outline badge-primary">
                {ligtningDetail?.region}
              </div>
              <div className="badge badge-outline badge-primary">
                {ligtningDetail?.recruitType}
              </div>

            <div className="avatar">
              <div className="w-24 rounded-full">
              //
                <img src={ligtningDetail.creatorId} alt={ligtningDetail.creatorId} />
              </div>
                <span>{ligtningDetail.creatorId}</span>
                <p>{ligtningDetail.description}</p>
            </div>
          </div>

          <div className="flex items-center gap-1">
          <h2>주의 사항</h2>
          <p></p>
          </div>

          <div className="flex items-center gap-1 hover:bg-base-200 p-1 rounded-md w-fit">
            <div>
                <span>{format(eventDate)}</span>
                <h3>{ligtningDetail.address}</h3>
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
            <div className="text-sm link">{routeDetail?.fileName}</div>
          </div>
          {routeDetail && (
            <KakaoMapRoute
              key={routeDetail.routeId}
              routePoints={routeDetail.routePoint}
            />
          )}
          {routeDetail && <AltitudeChart routePoints={routeDetail.routePoint} />}


          <div className="flex items-center justify-center gap-1">
          //
            <span>참가 마감 인원까지 {routeDetail.capacity}명 남았습니다.</span>
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

            <p>참가인원</p>
            //
            {ligtningDetail?.lightningUserId.map((member, index) => (
                <div className="avatar">
                  <div className="w-24 rounded-full">
                  //
                    <img src={ligtningDetail.lightningUserId} alt={ligtningDetail.lightningUserId} />
                  </div>
                    <span>{ligtningDetail.lightningUserId}</span>
                </div>
            ))}
          </div>
        </div>
    );
}


export default LightningDetailPage;