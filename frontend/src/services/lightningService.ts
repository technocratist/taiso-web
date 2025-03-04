import { post, get } from "../api/request";

export interface LightningGetRequest {
  gender: string;
  level: string;
  bikeType: string;
  region: string;
  tags: string[];
}

// ResponseComponent 타입 정의
export interface Lightning {
  lightningId: number;
  creatorId: number;
  title: string;
  eventDate: string;
  duration: number;
  createdAt: string;
  status: string;
  capacity: number;
  gender: string;
  level: string;
  bikeType: string;
  tags: string[];
  address: string;
  routeImgId: string;
}

// LightningGetResponse 타입 정의
export interface LightningListResponse {
  content: Lightning[];
  pageNumber: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  last: boolean;
}

export interface LightningPostRequest {
  title: string;
  description: string;
  eventDate: string;
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

export interface LightningDetailGetResponse {
  lightningId: number;
  creatorId: number;

  title: string;
  description: string;
  eventDate: string;
  duration: number;
  createdAt: string;
  status: string;
  capacity: number;
  latitude: number;
  longitude: number;

  gender: string;
  level: string;
  bikeType: string;
  region: string;
  recruitType: string;

  distance: number;
  routeId: number;
  address: string;
  routeImgId: string;
  // 클럽 관련
  isClubOnly: boolean;
  clubId: number;

  // 번개 참여자
  lightningUserId: number;

  // 번개 태그
  tagId: number;
  lightningTag: string[];
}

const createLightning = async (
  payload: LightningPostRequest
): Promise<LightningPostResponse> => {
  return await post("/lightnings", payload);
};

const getLightningList = async (
  page: number,
  size: number,
  sort: string,
  gender: string,
  bikeType: string,
  level: string,
  region: string,
  tags: string[],
  selectedDate: string
): Promise<LightningListResponse> => {
  return await get(
    `/lightnings/?page=${page}&size=${size}&sort=${sort}&gender=${gender}&bikeType=${bikeType}&level=${level}&region=${region}&tags=${tags}&date=${selectedDate}`
  );
};

const getLightningDetail = async (
  lightningId: number
): Promise<LightningDetailGetResponse> => {
  return await get(`/lightnings/${lightningId}`);
};

export default {
  createLightning,
  getLightningList,
  getLightningDetail,
};
