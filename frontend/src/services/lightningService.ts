import { post, get, del } from "../api/request";

export interface LightningGetRequest {
  gender: String;
  level: String;
  bikeType: String;
  region: String;
  tags: String[];
}

// ResponseComponent 타입 정의
export interface ResponseComponent {
  lightningId: number;
  creatorId: number;
  title: String;
  eventDate: String;
  duration: number;
  createdAt: String;
  status: String;
  capacity: number;
  gender: String;
  level: String;
  bikeType: String;
  tags: String[];
  address: String;
  routeImgId: String;
}

// LightningGetResponse 타입 정의
export interface LightningListResponse {
  content: ResponseComponent[];
  pageNumber: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  last: boolean;
}

export interface LightningPostRequest {
  title: String;
  description: String;
  eventDate: String;
  duration: number;
  capacity: number;
  latitude: number;
  longitude: number;
  status: string;
  gender: string;
  level: string;
  recruitType: string;
  bikeType: string;
  region: string;
  distance: number;
  routeId: number;
  address: string;
  isClubOnly: boolean;
  clubId: number | null;
  tags: string[];
}

export interface LightningPostResponse {
  lightningId: number;
}

const createLightning = async (
  payload: LightningPostRequest
): Promise<LightningPostResponse> => {
  return await post("/lightnings", payload);
};

const getLightningList = async (
  page: number,
  size: number,
  sort: string
): Promise<LightningListResponse> => {
  return await get(`/lightnings/?page=${page}&size=${size}&sort=${sort}`);
};

export default {
  createLightning,
  getLightningList,
};