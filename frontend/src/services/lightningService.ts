import { post, get, del } from "../api/request";

export interface LightningGetRequest{
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
    isClubOnly:  Boolean;
    clubId: number;
    tags: String[];
};

export interface LightningPostResponse {
    lightningId: number;
    }

{/*번개 정보 리스트 JSON 불러오기*/}
{/*정렬 없음*/}
const getLightningList = async (
  page: number,
    size: number,
    sort: string
): Promise<RouteListResponse> => {
  return await get(
    `/lightnings/?page=${page}&size=${size}&sort=${sort}`
  );
};

export default {
 getLightningList
};