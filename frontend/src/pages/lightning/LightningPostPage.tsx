import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import MapModal from "../../components/MapModal";
import RouteModal from "../../components/RouteModal";

// 옵션 상수들
const STATUS_OPTIONS = ["모집", "마감"];
const GENDER_OPTIONS = ["자유", "남성", "여성"];
const LEVEL_OPTIONS = ["초보", "중급", "고급"];
const RECRUIT_TYPE_OPTIONS = ["참가형", "주최형"];
const BIKE_TYPE_OPTIONS = ["로드", "산악"];
const REGION_OPTIONS = [
  "서울",
  "경기",
  "인천",
  "강원",
  "충청",
  "전라",
  "경상",
  "제주",
];
const TAG_OPTIONS = ["한강", "라이딩", "초보환영", "모임", "자전거"];

interface FormErrors {
  title?: string;
  description?: string;
  eventDate?: string;
  duration?: string;
  status?: string;
  capacity?: string;
  latitude?: string;
  longitude?: string;
  gender?: string;
  level?: string;
  recruitType?: string;
  bikeType?: string;
  region?: string;
  distance?: string;
  routeId?: string;
  address?: string;
  tags?: string;
  clubId?: string;
}

function LightningPostPage() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [eventDate, setEventDate] = useState("");
  const [duration, setDuration] = useState("");
  const [status, setStatus] = useState("");
  const [capacity, setCapacity] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [gender, setGender] = useState("");
  const [level, setLevel] = useState("");
  const [recruitType, setRecruitType] = useState("");
  const [bikeType, setBikeType] = useState("");
  const [region, setRegion] = useState("");
  const [distance, setDistance] = useState("");
  const [routeId, setRouteId] = useState("");
  const [address, setAddress] = useState("");
  const [isClubOnly, setIsClubOnly] = useState(false);
  const [clubId, setClubId] = useState("");
  const [tags, setTags] = useState<string[]>([]);

  const [formErrors, setFormErrors] = useState<FormErrors>({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // 태그 토글 함수
  const handleTagToggle = (option: string) => {
    // 더미 데이터 설정
    setTags([]);
    setLatitude("");
    setLongitude("");
    setRouteId("");
    // 더미 데이터 설정 끝

    if (tags.includes(option)) {
      setTags(tags.filter((t) => t !== option));
    } else {
      setTags([...tags, option]);
    }
    setFormErrors((prev) => ({ ...prev, tags: "" }));
  };

  // 전체 필드 검증 함수
  const validateForm = (): boolean => {
    const errors: FormErrors = {};

    if (!title.trim()) errors.title = "제목은 필수입니다.";
    if (!description.trim()) errors.description = "설명은 필수입니다.";
    if (!eventDate) errors.eventDate = "이벤트 날짜는 필수입니다.";
    if (!duration) errors.duration = "지속 시간은 필수입니다.";
    if (!status) errors.status = "모집 상태를 선택해주세요.";
    if (!capacity) errors.capacity = "최대 인원 수를 입력해주세요.";
    if (!latitude) errors.latitude = "위도를 입력해주세요.";
    if (!longitude) errors.longitude = "경도를 입력해주세요.";
    if (!gender) errors.gender = "성별을 선택해주세요.";
    if (!level) errors.level = "레벨을 선택해주세요.";
    if (!recruitType) errors.recruitType = "모집 유형을 선택해주세요.";
    if (!bikeType) errors.bikeType = "자전거 종류를 선택해주세요.";
    if (!region) errors.region = "지역을 선택해주세요.";
    if (!distance) errors.distance = "거리를 입력해주세요.";
    if (!routeId) errors.routeId = "경로 ID를 입력해주세요.";
    if (!address.trim()) errors.address = "주소를 입력해주세요.";
    if (tags.length === 0)
      errors.tags = "최소 한 개 이상의 태그를 선택해주세요.";
    if (isClubOnly && !clubId)
      errors.clubId = "클럽 전용 이벤트인 경우 클럽 ID를 입력해주세요.";

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setServerError("");

    if (!validateForm()) return;

    // 숫자형 필드 변환 후 payload 구성
    const payload = {
      title,
      description,
      eventDate,
      duration: Number(duration),
      status,
      capacity: Number(capacity),
      latitude: Number(latitude),
      longitude: Number(longitude),
      gender,
      level,
      recruitType,
      bikeType,
      region,
      distance: Number(distance),
      routeId: Number(routeId),
      address,
      isClubOnly,
      clubId: clubId ? Number(clubId) : null,
      tags,
    };

    console.log("Payload:", payload);
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 1500));
      navigate("/lightning");
    } catch (error) {
      console.error("이벤트 등록 에러:", error);
      setServerError("이벤트 등록에 실패했습니다. 잠시 후 다시 시도해주세요.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* 지도 모달 */}
      <input type="checkbox" id="map_modal" className="modal-toggle" />
      <div className="modal" role="dialog">
        <div className="modal-box">
          <MapModal />
          <div className="modal-action">
            <label htmlFor="map_modal" className="btn">
              Close!
            </label>
          </div>
        </div>
      </div>
      {/* 루트 등록 모달 */}
      <input type="checkbox" id="route_modal" className="modal-toggle" />
      <div className="modal" role="dialog">
        <div className="modal-box">
          <RouteModal />
          <div className="modal-action">
            <label htmlFor="route_modal" className="btn">
              Close!
            </label>
          </div>
        </div>
      </div>
      {/* 번개 등록 폼 */}
      <div className="flex justify-center items-center relative sm:w-full w-[90%]">
        <div className="w-full max-w-lg bg-base-100 p-6">
          <h1 className="text-2xl font-bold text-center mb-4">번개 등록하기</h1>
          {serverError && (
            <p className="text-red-500 mb-4 text-center">{serverError}</p>
          )}
          <form onSubmit={handleSubmit} noValidate>
            {/* 제목 */}
            <div className="form-control mb-4">
              <label htmlFor="title" className="label">
                <span className="label-text">제목</span>
              </label>
              <input
                id="title"
                type="text"
                placeholder="예: 한강 라이딩"
                value={title}
                onChange={(e) => {
                  setTitle(e.target.value);
                  setFormErrors((prev) => ({ ...prev, title: "" }));
                }}
                className="input input-bordered placeholder:text-sm"
              />
              {formErrors.title && (
                <span className="text-red-500 mt-2 block">
                  {formErrors.title}
                </span>
              )}
            </div>

            {/* 설명 */}
            <div className="form-control mb-4">
              <label htmlFor="description" className="label">
                <span className="label-text">설명</span>
              </label>
              <textarea
                id="description"
                placeholder="예: 초보자도 환영! 함께 달려요."
                value={description}
                onChange={(e) => {
                  setDescription(e.target.value);
                  setFormErrors((prev) => ({ ...prev, description: "" }));
                }}
                className="textarea textarea-bordered placeholder:text-sm"
              ></textarea>
              {formErrors.description && (
                <span className="text-red-500 mt-2 block">
                  {formErrors.description}
                </span>
              )}
            </div>

            {/* 이벤트 날짜 */}
            <div className="form-control mb-4">
              <label htmlFor="eventDate" className="label">
                <span className="label-text">이벤트 날짜</span>
              </label>
              <input
                id="eventDate"
                type="datetime-local"
                value={eventDate}
                onChange={(e) => {
                  setEventDate(e.target.value);
                  setFormErrors((prev) => ({ ...prev, eventDate: "" }));
                }}
                className="input input-bordered"
              />
              {formErrors.eventDate && (
                <span className="text-red-500 mt-2 block">
                  {formErrors.eventDate}
                </span>
              )}
            </div>

            {/* 지속 시간과 모집 상태 */}
            <div className="grid grid-cols-2 gap-4 mb-4">
              <div className="form-control">
                <label htmlFor="duration" className="label">
                  <span className="label-text">지속 시간 (분)</span>
                </label>
                <input
                  id="duration"
                  type="number"
                  placeholder="예: 120"
                  value={duration}
                  onChange={(e) => {
                    setDuration(e.target.value);
                    setFormErrors((prev) => ({ ...prev, duration: "" }));
                  }}
                  className="input input-bordered placeholder:text-sm"
                />
                {formErrors.duration && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.duration}
                  </span>
                )}
              </div>
              <div className="form-control">
                <label htmlFor="status" className="label">
                  <span className="label-text">모집 상태</span>
                </label>
                <select
                  id="status"
                  value={status}
                  onChange={(e) => {
                    setStatus(e.target.value);
                    setFormErrors((prev) => ({ ...prev, status: "" }));
                  }}
                  className="select select-bordered"
                >
                  <option value="">선택하세요</option>
                  {STATUS_OPTIONS.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
                {formErrors.status && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.status}
                  </span>
                )}
              </div>
            </div>

            {/* 최대 인원, 위도 */}
            <div className="grid grid-cols-2 gap-4 mb-4">
              <div className="form-control">
                <label htmlFor="capacity" className="label">
                  <span className="label-text">최대 인원</span>
                </label>
                <input
                  id="capacity"
                  type="number"
                  placeholder="예: 10"
                  value={capacity}
                  onChange={(e) => {
                    setCapacity(e.target.value);
                    setFormErrors((prev) => ({ ...prev, capacity: "" }));
                  }}
                  className="input input-bordered placeholder:text-sm"
                />
                {formErrors.capacity && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.capacity}
                  </span>
                )}
              </div>
              <label htmlFor="map_modal" className="btn">
                주소 찾는 모달 열기
              </label>
            </div>

            {/* 지역 */}
            <div className="mb-4">
              <div className="form-control">
                <label className="label">
                  <span className="label-text">지역</span>
                </label>
                <div className="flex gap-2 flex-wrap">
                  {REGION_OPTIONS.map((option) => (
                    <button
                      type="button"
                      key={option}
                      onClick={() => {
                        setRegion(option);
                        setFormErrors((prev) => ({ ...prev, region: "" }));
                      }}
                      className={`btn btn-sm ${
                        region === option ? "btn-primary" : "btn-outline"
                      }`}
                    >
                      {option}
                    </button>
                  ))}
                </div>
                {formErrors.region && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.region}
                  </span>
                )}
              </div>
            </div>

            {/* 성별, 레벨 */}
            <div className="grid grid-cols-2 gap-4 mb-4">
              <div className="form-control">
                <label htmlFor="gender" className="label">
                  <span className="label-text">성별</span>
                </label>
                <select
                  id="gender"
                  value={gender}
                  onChange={(e) => {
                    setGender(e.target.value);
                    setFormErrors((prev) => ({ ...prev, gender: "" }));
                  }}
                  className="select select-bordered"
                >
                  <option value="">선택하세요</option>
                  {GENDER_OPTIONS.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
                {formErrors.gender && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.gender}
                  </span>
                )}
              </div>
              <div className="form-control">
                <label htmlFor="level" className="label">
                  <span className="label-text">레벨</span>
                </label>
                <select
                  id="level"
                  value={level}
                  onChange={(e) => {
                    setLevel(e.target.value);
                    setFormErrors((prev) => ({ ...prev, level: "" }));
                  }}
                  className="select select-bordered"
                >
                  <option value="">선택하세요</option>
                  {LEVEL_OPTIONS.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
                {formErrors.level && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.level}
                  </span>
                )}
              </div>
            </div>

            {/* 모집 유형, 자전거 종류 */}
            <div className="grid grid-cols-2 gap-4 mb-4">
              <div className="form-control">
                <label htmlFor="recruitType" className="label">
                  <span className="label-text">모집 유형</span>
                </label>
                <select
                  id="recruitType"
                  value={recruitType}
                  onChange={(e) => {
                    setRecruitType(e.target.value);
                    setFormErrors((prev) => ({ ...prev, recruitType: "" }));
                  }}
                  className="select select-bordered"
                >
                  <option value="">선택하세요</option>
                  {RECRUIT_TYPE_OPTIONS.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
                {formErrors.recruitType && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.recruitType}
                  </span>
                )}
              </div>
              <div className="form-control">
                <label htmlFor="bikeType" className="label">
                  <span className="label-text">자전거 종류</span>
                </label>
                <select
                  id="bikeType"
                  value={bikeType}
                  onChange={(e) => {
                    setBikeType(e.target.value);
                    setFormErrors((prev) => ({ ...prev, bikeType: "" }));
                  }}
                  className="select select-bordered"
                >
                  <option value="">선택하세요</option>
                  {BIKE_TYPE_OPTIONS.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
                {formErrors.bikeType && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.bikeType}
                  </span>
                )}
              </div>
            </div>

            {/* 거리, 경로 ID */}
            <div className="grid grid-cols-2 gap-4 mb-4">
              <div className="form-control">
                <label htmlFor="distance" className="label">
                  <span className="label-text">거리 (km)</span>
                </label>
                <input
                  id="distance"
                  type="number"
                  placeholder="예: 25"
                  value={distance}
                  onChange={(e) => {
                    setDistance(e.target.value);
                    setFormErrors((prev) => ({ ...prev, distance: "" }));
                  }}
                  className="input input-bordered placeholder:text-sm"
                />
                {formErrors.distance && (
                  <span className="text-red-500 mt-2 block">
                    {formErrors.distance}
                  </span>
                )}
              </div>
              <label htmlFor="route_modal" className="btn">
                경로 등록
              </label>
            </div>

            {/* 주소 */}
            <div className="form-control mb-4">
              <label htmlFor="address" className="label">
                <span className="label-text">주소</span>
              </label>
              <input
                id="address"
                type="text"
                placeholder="예: 서울특별시 강남구"
                value={address}
                onChange={(e) => {
                  setAddress(e.target.value);
                  setFormErrors((prev) => ({ ...prev, address: "" }));
                }}
                className="input input-bordered placeholder:text-sm"
              />
              {formErrors.address && (
                <span className="text-red-500 mt-2 block">
                  {formErrors.address}
                </span>
              )}
            </div>

            {/* 클럽 전용 여부 및 클럽 ID */}
            <div className="form-control mb-4">
              <label className="cursor-pointer label">
                <span className="label-text">클럽 전용 이벤트</span>
                <input
                  type="checkbox"
                  checked={isClubOnly}
                  onChange={(e) => setIsClubOnly(e.target.checked)}
                  className="checkbox checkbox-primary"
                />
              </label>
              {isClubOnly && (
                <div className="mt-2">
                  <label htmlFor="clubId" className="label">
                    <span className="label-text">클럽 ID</span>
                  </label>
                  <input
                    id="clubId"
                    type="number"
                    placeholder="클럽 ID 입력"
                    value={clubId}
                    onChange={(e) => {
                      setClubId(e.target.value);
                      setFormErrors((prev) => ({ ...prev, clubId: "" }));
                    }}
                    className="input input-bordered placeholder:text-sm"
                  />
                  {formErrors.clubId && (
                    <span className="text-red-500 mt-2 block">
                      {formErrors.clubId}
                    </span>
                  )}
                </div>
              )}
            </div>

            {/* 태그 */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">태그</span>
              </label>
              <div className="flex gap-2 flex-wrap">
                {TAG_OPTIONS.map((option) => (
                  <button
                    key={option}
                    type="button"
                    onClick={() => handleTagToggle(option)}
                    className={`btn btn-sm ${
                      tags.includes(option) ? "btn-primary" : "btn-outline"
                    }`}
                  >
                    {option}
                  </button>
                ))}
              </div>
              {formErrors.tags && (
                <span className="text-red-500 mt-2 block">
                  {formErrors.tags}
                </span>
              )}
            </div>

            {/* 제출 버튼 */}
            <div className="form-control mt-6 mb-16">
              <button
                type="submit"
                className="btn btn-primary"
                disabled={loading}
              >
                {loading ? "등록 중..." : "이벤트 등록"}
              </button>
            </div>
          </form>
        </div>
        {loading && (
          <div className="absolute inset-0 flex flex-col justify-center items-center z-50">
            <span
              className="loading loading-spinner loading-lg"
              aria-label="Loading"
            ></span>
            <span className="mt-4">
              번개 등록 중입니다. 잠시 기다려주세요...
            </span>
          </div>
        )}
      </div>
    </>
  );
}

export default LightningPostPage;
