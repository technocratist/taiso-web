import { post } from "../api/request";

export interface RoutePostRequest {
  routeData: string;
  file: File;
}

export interface RoutePostResponse {
  message: string;
}

const createRoute = async (
  payload: RoutePostRequest
): Promise<RoutePostResponse> => {
  const formData = new FormData();
  // JSON 데이터를 Blob으로 감싸고 application/json 타입 지정
  formData.append(
    "routeData",
    new Blob([payload.routeData], { type: "application/json" }),
    "routeData.json"
  );

  // 파일의 MIME 타입이 없거나 'application/octet-stream'이면 지원되는 타입으로 대체
  const fileType =
    payload.file.type && payload.file.type !== "application/octet-stream"
      ? payload.file.type
      : "image/png"; // 상황에 맞게 변경 (예: "image/jpeg", "application/pdf" 등)
  const fileWithType = new File([payload.file], payload.file.name, {
    type: fileType,
  });
  formData.append("file", fileWithType, fileWithType.name);

  // axios나 fetch를 사용할 때 Content-Type 헤더를 직접 지정하지 않도록 합니다.
  return await post<RoutePostResponse>("/routes", formData);
};

export default {
  createRoute,
};
