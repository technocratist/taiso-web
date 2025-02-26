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
  status: String;
  capacity: number;
  latitude: number;
  longitude: number;
  gender: String;
  level: String;
  recruitType: String;
  bikeType: String;
  region: String;
  distance: number;
  routeId: number;
  address: String;
  isClubOnly: Boolean;
  clubId: number;
  tags: String[];
}

export interface LightningPostResponse {
  lightningId: number;
}

export interface LightningDetailGetResponse {
    lightningId: number
    creatorId: number
    title: String
    description: String
    eventDate: String
    duration: number
    createdAt: String
    // updatedAt: String
    status: String
    capacity: number
    latitude: number
    longitude: number
    gender: String
    level: String
    bikeType: String
    region: String
    recruitType: String

    distance: number
    routeId: number
    address: String
    // 연결 클럽
    isClubOnly: Boolean
    clubId: number
    // 번개 참여자
    lightningUserId: number
    //  List<LightningDetailMemberDTO> member;
    // 번개 태그
    tagId: number
    lightningTag: String[]
}


// 번개 정보 리스트 JSON 불러오기
// 정렬 없음
const getLightningList = async (
  page: number,
  size: number,
  sort: string
): Promise<LightningListResponse> => {
  return await get(`/lightnings/?page=${page}&size=${size}&sort=${sort}`);
};

const getLightningDetail = async (
  ligtningId: number
): Promise<LightningDetailGetResponse> => {
  return await get(`/lightnings/${ligtningId}`);
};

export default {
  getLightningList,
  getLightningDetail,
};
