import React, { useState } from "react";

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

    const formData = new FormData();
    formData.append("routeName", routeName);
    formData.append("userId", userId);
    formData.append("description", description);
    // 태그 입력 값을 콤마로 구분하여 배열 형태로 변환한 후 JSON 문자열로 전송
    formData.append("tag", JSON.stringify(tag.split(",").map((t) => t.trim())));
    formData.append("region", region);
    formData.append("distanceType", distanceType);
    formData.append("altitudeType", altitudeType);
    formData.append("roadType", roadType);
    if (selectedFile) {
      formData.append("file", selectedFile);
    }

    try {
      const response = await fetch("/api/route", {
        method: "POST",
        body: formData,
      });
      if (response.ok) {
        alert("Route posted successfully!");
      } else {
        alert("Failed to post route");
      }
    } catch (error) {
      console.error("Error posting route:", error);
      alert("Error occurred");
    }
  };

  return (
    <div>
      <h1>Post a Route</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Route Name:</label>
          <input
            type="text"
            value={routeName}
            onChange={(e) => setRouteName(e.target.value)}
            required
          />
        </div>
        <div>
          <label>User ID:</label>
          <input
            type="number"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Description:</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Tag (콤마로 구분):</label>
          <input
            type="text"
            value={tag}
            onChange={(e) => setTag(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Region:</label>
          <input
            type="text"
            value={region}
            onChange={(e) => setRegion(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Distance Type:</label>
          <input
            type="text"
            value={distanceType}
            onChange={(e) => setDistanceType(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Altitude Type:</label>
          <input
            type="text"
            value={altitudeType}
            onChange={(e) => setAltitudeType(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Road Type:</label>
          <input
            type="text"
            value={roadType}
            onChange={(e) => setRoadType(e.target.value)}
            required
          />
        </div>
        <div>
          <label>File:</label>
          <input type="file" onChange={handleFileChange} />
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default RoutePostPage;
