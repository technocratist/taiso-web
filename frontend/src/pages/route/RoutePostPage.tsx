import React, { useState } from "react";
import { useNavigate } from "react-router";
import routeService from "../../services/routeService";
import usePost from "../../hooks/usePost";

// enum 형식의 옵션들 (필요에 따라 실제 값으로 수정)
const TAG_OPTIONS = [
  "한강 자전거길",
  "국토종주길",
  "산악 도로",
  "평지 도로",
  "위험한 공도",
];

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
const DISTANCE_TYPE_OPTIONS = ["단거리", "중거리", "장거리"];
const ALTITUDE_TYPE_OPTIONS = ["낮음", "중간", "높음"];
const ROAD_TYPE_OPTIONS = ["평지", "산길", "고속도로"];

function RoutePostPage() {
  const [routeName, setRouteName] = useState("");
  const [description, setDescription] = useState("");
  // tag는 여러 옵션 선택이 가능하므로 배열 상태로 관리
  const [tags, setTags] = useState<string[]>([]);
  const [region, setRegion] = useState("");
  const [distanceType, setDistanceType] = useState("");
  const [altitudeType, setAltitudeType] = useState("");
  const [roadType, setRoadType] = useState("");
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  // 폼 단위의 에러 메시지 상태
  const [formError, setFormError] = useState("");
  const navigate = useNavigate();

  // usePost 훅
  const {
    executePost,
    loading,
    error: postError,
  } = usePost(routeService.createRoute);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
    }
  };

  // Add the new toggle function for multi-select tags
  const handleTagToggle = (option: string) => {
    if (tags.includes(option)) {
      setTags(tags.filter((t) => t !== option));
    } else {
      setTags([...tags, option]);
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setFormError(""); // 에러 초기화

    // 필수 항목 검증
    if (!selectedFile) {
      setFormError("Please select a file.");
      return;
    }
    if (!routeName.trim()) {
      setFormError("Route Name is required.");
      return;
    }

    if (!description.trim()) {
      setFormError("Description is required.");
      return;
    }
    if (tags.length === 0) {
      setFormError("At least one Tag must be selected.");
      return;
    }
    if (!region) {
      setFormError("Region is required.");
      return;
    }
    if (!distanceType) {
      setFormError("Distance Type is required.");
      return;
    }
    if (!altitudeType) {
      setFormError("Altitude Type is required.");
      return;
    }
    if (!roadType) {
      setFormError("Road Type is required.");
      return;
    }

    // 백엔드 스펙에 맞게 데이터 구성
    const payload = {
      routeData: JSON.stringify({
        routeName,
        description,
        tag: tags,
        region,
        distanceType,
        altitudeType,
        roadType,
      }),
      file: selectedFile,
    };

    try {
      await executePost(payload);
      navigate("/");
    } catch (err) {
      console.error("Error posting route:", err);
      setFormError("Failed to post route. Please try again later.");
    }
  };

  return (
    <div className="flex justify-center items-center bg-base-200 relative mt-12">
      <div className="card w-full max-w-lg shadow-xl bg-base-100">
        <div className="card-body">
          <h1 className="text-2xl font-bold mb-4 text-center">Post a Route</h1>
          {/* 폼 단위 에러 메시지 */}
          {formError && (
            <span className="text-red-500 mb-4 block">{formError}</span>
          )}
          {/* API 요청 에러 메시지 */}
          {postError && (
            <span className="text-red-500 mb-4 block">
              {typeof postError === "string" ? postError : "An error occurred."}
            </span>
          )}
          <form onSubmit={handleSubmit} noValidate>
            {/* Route Name */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Route Name:</span>
              </label>
              <input
                type="text"
                placeholder="Route Name"
                value={routeName}
                onChange={(e) => setRouteName(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>

            {/* Description */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Description:</span>
              </label>
              <textarea
                placeholder="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="textarea textarea-bordered"
                required
              />
            </div>
            {/* Tags (다중 선택) using DaisyUI toggles */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Tags:</span>
              </label>
              <div className="flex gap-2 flex-wrap">
                {TAG_OPTIONS.map((option) => (
                  <button
                    key={option}
                    type="button"
                    className={`btn ${
                      tags.includes(option) ? "btn-primary" : "btn-outline"
                    }`}
                    onClick={() => handleTagToggle(option)}
                  >
                    {option}
                  </button>
                ))}
              </div>
            </div>
            {/* Region */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Region:</span>
              </label>
              <div className="flex gap-2 flex-wrap">
                {REGION_OPTIONS.map((option) => (
                  <button
                    key={option}
                    type="button"
                    className={`btn ${
                      region === option ? "btn-primary" : "btn-outline"
                    }`}
                    onClick={() => setRegion(option)}
                  >
                    {option}
                  </button>
                ))}
              </div>
            </div>
            {/* Distance Type */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Distance Type:</span>
              </label>
              <div className="flex gap-2 flex-wrap">
                {DISTANCE_TYPE_OPTIONS.map((option) => (
                  <button
                    key={option}
                    type="button"
                    className={`btn ${
                      distanceType === option ? "btn-primary" : "btn-outline"
                    }`}
                    onClick={() => setDistanceType(option)}
                  >
                    {option}
                  </button>
                ))}
              </div>
            </div>
            {/* Altitude Type */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Altitude Type:</span>
              </label>
              <div className="flex gap-2 flex-wrap">
                {ALTITUDE_TYPE_OPTIONS.map((option) => (
                  <button
                    key={option}
                    type="button"
                    className={`btn ${
                      altitudeType === option ? "btn-primary" : "btn-outline"
                    }`}
                    onClick={() => setAltitudeType(option)}
                  >
                    {option}
                  </button>
                ))}
              </div>
            </div>
            {/* Road Type */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Road Type:</span>
              </label>
              <div className="flex gap-2 flex-wrap">
                {ROAD_TYPE_OPTIONS.map((option) => (
                  <button
                    key={option}
                    type="button"
                    className={`btn ${
                      roadType === option ? "btn-primary" : "btn-outline"
                    }`}
                    onClick={() => setRoadType(option)}
                  >
                    {option}
                  </button>
                ))}
              </div>
            </div>
            {/* File Input */}
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">File:</span>
              </label>
              <input
                type="file"
                onChange={handleFileChange}
                className="file-input file-input-bordered w-full"
                required
              />
            </div>
            {/* Submit Button */}
            <div className="form-control mt-6">
              <button
                type="submit"
                className="btn btn-primary"
                disabled={loading}
              >
                {loading ? "Submitting..." : "Submit"}
              </button>
            </div>
          </form>
        </div>
      </div>
      {/* 로딩 오버레이 */}
      {loading && (
        <div className="absolute inset-0 z-50 flex justify-center items-center bg-gray-500 bg-opacity-50">
          <span className="loading loading-spinner loading-lg"></span>
        </div>
      )}
    </div>
  );
}

export default RoutePostPage;
