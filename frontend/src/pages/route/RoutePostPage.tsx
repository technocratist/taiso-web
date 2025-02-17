import React, { useState } from "react";
import routeService from "../../services/routeService";

function RoutePostPage() {
  const [routeName, setRouteName] = useState("");
  const [userId, setUserId] = useState("");
  const [description, setDescription] = useState("");
  const [tag, setTag] = useState("");
  const [region, setRegion] = useState("");
  const [distanceType, setDistanceType] = useState("");
  const [altitudeType, setAltitudeType] = useState("");
  const [roadType, setRoadType] = useState("");
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(123);
    // 파일이 필수인 경우, 파일 선택 여부를 확인합니다.
    if (!selectedFile) {
      alert("Please select a file.");
      return;
    }

    // 백엔드 RoutePostRequestDTO 스펙에 맞게 데이터 구성 (userId는 number로 변환)
    const payload = {
      routeData: JSON.stringify({
        routeName,
        userId: Number(userId),
        description,
        tag: tag.split(",").map((t) => t.trim()),
        region,
        distanceType,
        altitudeType,
        roadType,
      }),
      file: selectedFile,
    };

    try {
      await routeService.createRoute(payload);
      alert("Route posted successfully!");
    } catch (error) {
      console.error("Error posting route:", error);
      alert("Failed to post route");
    }
  };

  return (
    <div className="flex justify-center items-center bg-base-200">
      <div className="card w-full max-w-lg shadow-xl bg-base-100">
        <div className="card-body">
          <h1 className="text-2xl font-bold mb-4 text-center">Post a Route</h1>
          <form onSubmit={handleSubmit} noValidate>
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
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">User ID:</span>
              </label>
              <input
                type="number"
                placeholder="User ID"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>
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
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Tag (콤마로 구분):</span>
              </label>
              <input
                type="text"
                placeholder="Tags (comma separated)"
                value={tag}
                onChange={(e) => setTag(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Region:</span>
              </label>
              <input
                type="text"
                placeholder="Region"
                value={region}
                onChange={(e) => setRegion(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Distance Type:</span>
              </label>
              <input
                type="text"
                placeholder="Distance Type"
                value={distanceType}
                onChange={(e) => setDistanceType(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Altitude Type:</span>
              </label>
              <input
                type="text"
                placeholder="Altitude Type"
                value={altitudeType}
                onChange={(e) => setAltitudeType(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">Road Type:</span>
              </label>
              <input
                type="text"
                placeholder="Road Type"
                value={roadType}
                onChange={(e) => setRoadType(e.target.value)}
                className="input input-bordered"
                required
              />
            </div>
            <div className="form-control mb-4">
              <label className="label">
                <span className="label-text">File:</span>
              </label>
              <input
                type="file"
                onChange={handleFileChange}
                className="file-input file-input-bordered w-full"
              />
            </div>
            <div className="form-control mt-6">
              <button type="submit" className="btn btn-primary">
                Submit
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default RoutePostPage;
