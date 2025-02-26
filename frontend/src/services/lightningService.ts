import { post } from "../api/request";

export interface LightningRequest {
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

export interface LightningResponse {
  lightningId: number;
}

const createLightning = async (
  payload: LightningRequest
): Promise<LightningResponse> => {
  return await post("/lightnings", payload);
};

export const lightningService = {
  createLightning,
};
