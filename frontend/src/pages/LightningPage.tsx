import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import MainNavbar from "../components/MainNavbar";
import LightningList from "../components/LightningList";
import DateCarousel from "../components/DateCarousel";

// 성별 필터 옵션
const genderTags: string[] = ["남", "여", "자유"];

// 자전거 타입 필터 옵션
const bikeTypeTags: string[] = ["로드", "MTB", "하이브리드", "기타"];

// 난이도 필터 옵션
const levelTags: string[] = ["입문", "초급", "중급", "고급"];

// 지역 필터 옵션
const locationTags: string[] = [
  "서울",
  "경기",
  "인천",
  "강원",
  "충청",
  "전라",
  "경상",
  "제주",
];

// 일반 태그 옵션
const availableTags: string[] = [
  "샤방법",
  "오픈라이딩",
  "따폭연",
  "장거리",
  "따릉이",
  "친목",
];

function LightningPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const sort: string = searchParams.get("sort") || "eventDate";
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());

  // 일반 태그 (다중 선택)
  const [selectedAvailableTags, setSelectedAvailableTags] = useState<string[]>(
    searchParams.get("tags")?.split(",") || []
  );

  // 단일 선택 태그들
  const [selectedGender, setSelectedGender] = useState<string | null>(
    searchParams.get("gender")
  );
  const [selectedBikeType, setSelectedBikeType] = useState<string | null>(
    searchParams.get("bikeType")
  );
  const [selectedLevel, setSelectedLevel] = useState<string | null>(
    searchParams.get("level")
  );
  const [selectedLocation, setSelectedLocation] = useState<string | null>(
    searchParams.get("region")
  );

  // 일반 태그 토글 함수
  const toggleAvailableTag = (tag: string): void => {
    let newTags;
    if (selectedAvailableTags.includes(tag)) {
      newTags = selectedAvailableTags.filter((t) => t !== tag);
    } else {
      newTags = [...selectedAvailableTags, tag];
    }
    setSelectedAvailableTags(newTags);
    updateSearchParams({ tags: newTags.length > 0 ? newTags.join(",") : null });
  };

  // 단일 선택 토글 함수들
  const toggleGenderTag = (tag: string): void => {
    const newGender = selectedGender === tag ? null : tag;
    setSelectedGender(newGender);
    updateSearchParams({ gender: newGender });
  };

  const toggleBikeTypeTag = (tag: string): void => {
    const newBikeType = selectedBikeType === tag ? null : tag;
    setSelectedBikeType(newBikeType);
    updateSearchParams({ bikeType: newBikeType });
  };

  const toggleLevelTag = (tag: string): void => {
    const newLevel = selectedLevel === tag ? null : tag;
    setSelectedLevel(newLevel);
    updateSearchParams({ level: newLevel });
  };

  const toggleLocationTag = (tag: string): void => {
    const newLocation = selectedLocation === tag ? null : tag;
    setSelectedLocation(newLocation);
    updateSearchParams({ region: newLocation });
  };

  // 검색 파라미터 업데이트 함수
  const updateSearchParams = (updates: Record<string, string | null>): void => {
    const newParams = new URLSearchParams(searchParams.toString());

    Object.entries(updates).forEach(([key, value]) => {
      if (value === null || value === "") {
        newParams.delete(key);
      } else {
        newParams.set(key, value);
      }
    });

    setSearchParams(newParams);
  };

  // // 정렬 링크 생성 함수
  // const getSortLink = (newSort: string): string => {
  //   const params = new URLSearchParams(searchParams.toString());
  //   params.set("sort", newSort);
  //   return `/lightning?${params.toString()}`;
  // };

  return (
    <div className="min-h-screen flex flex-col">
      <MainNavbar />
      <DateCarousel onDateChange={(date) => setSelectedDate(date)} />

      <div className="flex-1 w-full mx-auto px-4 sm:px-6">
        {/* 필터 드롭다운 섹션 */}
        <div className="grid grid-cols-2 md:grid-cols-5 gap-2 mb-4">
          {/* 태그 드롭다운 (다중 선택) */}
          <div className="dropdown dropdown-hover">
            <div tabIndex={0} role="button" className="btn btn-sm m-1">
              태그
              {selectedAvailableTags.length > 0 && (
                <span className="badge badge-sm badge-primary ml-1">
                  {selectedAvailableTags.length}
                </span>
              )}
            </div>
            <ul
              tabIndex={0}
              className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52 max-h-96 overflow-y-auto"
            >
              {availableTags.map((tag) => (
                <li key={tag}>
                  <a
                    onClick={() => toggleAvailableTag(tag)}
                    className={
                      selectedAvailableTags.includes(tag) ? "active" : ""
                    }
                  >
                    {tag}
                    {selectedAvailableTags.includes(tag) && (
                      <span className="badge badge-primary badge-sm">✓</span>
                    )}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* 성별 드롭다운 (단일 선택) */}
          <div className="dropdown dropdown-hover">
            <div tabIndex={0} role="button" className="btn btn-sm m-1">
              성별
              {selectedGender && (
                <span className="badge badge-sm badge-primary ml-1">
                  {selectedGender}
                </span>
              )}
            </div>
            <ul
              tabIndex={0}
              className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52"
            >
              {genderTags.map((tag) => (
                <li key={tag}>
                  <a
                    onClick={() => toggleGenderTag(tag)}
                    className={selectedGender === tag ? "active" : ""}
                  >
                    {tag}
                    {selectedGender === tag && (
                      <span className="badge badge-primary badge-sm">✓</span>
                    )}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* 자전거 타입 드롭다운 (단일 선택) */}
          <div className="dropdown dropdown-hover">
            <div tabIndex={0} role="button" className="btn btn-sm m-1">
              자전거 타입
              {selectedBikeType && (
                <span className="badge badge-sm badge-primary ml-1">
                  {selectedBikeType}
                </span>
              )}
            </div>
            <ul
              tabIndex={0}
              className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52"
            >
              {bikeTypeTags.map((tag) => (
                <li key={tag}>
                  <a
                    onClick={() => toggleBikeTypeTag(tag)}
                    className={selectedBikeType === tag ? "active" : ""}
                  >
                    {tag}
                    {selectedBikeType === tag && (
                      <span className="badge badge-primary badge-sm">✓</span>
                    )}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* 난이도 드롭다운 (단일 선택) */}
          <div className="dropdown dropdown-hover">
            <div tabIndex={0} role="button" className="btn btn-sm m-1">
              난이도
              {selectedLevel && (
                <span className="badge badge-sm badge-primary ml-1">
                  {selectedLevel}
                </span>
              )}
            </div>
            <ul
              tabIndex={0}
              className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52"
            >
              {levelTags.map((tag) => (
                <li key={tag}>
                  <a
                    onClick={() => toggleLevelTag(tag)}
                    className={selectedLevel === tag ? "active" : ""}
                  >
                    {tag}
                    {selectedLevel === tag && (
                      <span className="badge badge-primary badge-sm">✓</span>
                    )}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* 지역 드롭다운 (단일 선택) */}
          <div className="dropdown dropdown-hover">
            <div tabIndex={0} role="button" className="btn btn-sm m-1">
              지역
              {selectedLocation && (
                <span className="badge badge-sm badge-primary ml-1">
                  {selectedLocation}
                </span>
              )}
            </div>
            <ul
              tabIndex={0}
              className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52 max-h-96 overflow-y-auto"
            >
              {locationTags.map((tag) => (
                <li key={tag}>
                  <a
                    onClick={() => toggleLocationTag(tag)}
                    className={selectedLocation === tag ? "active" : ""}
                  >
                    {tag}
                    {selectedLocation === tag && (
                      <span className="badge badge-primary badge-sm">✓</span>
                    )}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        </div>

        {/* 선택된 필터 태그 표시 영역 */}
        {(selectedAvailableTags.length > 0 ||
          selectedGender ||
          selectedBikeType ||
          selectedLevel ||
          selectedLocation) && (
          <div className="flex flex-wrap gap-2 mb-4">
            {selectedAvailableTags.map((tag) => (
              <span key={tag} className="badge badge-primary gap-1">
                {tag}
                <button onClick={() => toggleAvailableTag(tag)}>×</button>
              </span>
            ))}
            {selectedGender && (
              <span className="badge badge-primary gap-1">
                성별: {selectedGender}
                <button onClick={() => toggleGenderTag(selectedGender)}>
                  ×
                </button>
              </span>
            )}
            {selectedBikeType && (
              <span className="badge badge-primary gap-1">
                자전거: {selectedBikeType}
                <button onClick={() => toggleBikeTypeTag(selectedBikeType)}>
                  ×
                </button>
              </span>
            )}
            {selectedLevel && (
              <span className="badge badge-primary gap-1">
                난이도: {selectedLevel}
                <button onClick={() => toggleLevelTag(selectedLevel)}>×</button>
              </span>
            )}
            {selectedLocation && (
              <span className="badge badge-primary gap-1">
                지역: {selectedLocation}
                <button onClick={() => toggleLocationTag(selectedLocation)}>
                  ×
                </button>
              </span>
            )}

            {/* 전체 초기화 버튼 */}
            {(selectedAvailableTags.length > 0 ||
              selectedGender ||
              selectedBikeType ||
              selectedLevel ||
              selectedLocation) && (
              <button
                className="badge badge-outline"
                onClick={() => {
                  setSelectedAvailableTags([]);
                  setSelectedGender(null);
                  setSelectedBikeType(null);
                  setSelectedLevel(null);
                  setSelectedLocation(null);
                  setSearchParams(new URLSearchParams({ sort }));
                }}
              >
                전체 초기화
              </button>
            )}
          </div>
        )}

        <div className="fixed bottom-8 right-10 z-50">
          {/*번개 생성 버튼*/}
          <Link to="/lightning/post" className="btn btn-primary btn-circle">
            <svg
              data-slot="icon"
              fill="currentColor"
              className="size-8"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path d="M10.75 4.75a.75.75 0 0 0-1.5 0v4.5h-4.5a.75.75 0 0 0 0 1.5h4.5v4.5a.75.75 0 0 0 1.5 0v-4.5h4.5a.75.75 0 0 0 0-1.5h-4.5v-4.5Z"></path>
            </svg>
          </Link>
        </div>

        {/* LightningList에 필터 옵션 전달 */}
        <LightningList
          sort={sort}
          gender={searchParams.get("gender") || ""}
          bikeType={searchParams.get("bikeType") || ""}
          level={searchParams.get("level") || ""}
          region={searchParams.get("region") || ""}
          tags={searchParams.get("tags")?.split(",") || []}
          selectedDate={selectedDate}
        />
      </div>
    </div>
  );
}

export default LightningPage;
