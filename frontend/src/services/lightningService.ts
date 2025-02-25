import { post } from "../api/request";

export interface LightningRequest {
  title: string;
  description: string;
  eventDate: string;
  duration: number;
  status: string;
  capacity: number;
  latitude: number;
  longitude: number;
  gender: string;
  level: string;
  recruitType: string;
  bikeType: string;
  region: string;
  distance: number;
  routeId: number;
  address: string;
  isClubOnly: boolean;
  clubId: number;
  tags: string[];
}

export interface LightningResponse {
  lightningId: number;
}

const createLightning = async (
  payload: LightningRequest
): Promise<LightningResponse> => {
  return await post("/lightning/post", payload);
};

export const lightningService = {
  createLightning,
};
