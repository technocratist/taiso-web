// src/components/NaverMap.tsx
import React, { useEffect, useRef } from "react";
import { RouteDetailResponse } from "../services/routeService";
import useScript from "../hooks/useScript";

interface NaverMapProps {
  routeData: RouteDetailResponse;
}

const DEFAULT_COORD = { lat: 37.5665, lng: 126.978 };

const NaverMap: React.FC<NaverMapProps> = ({ routeData }) => {
  const mapRef = useRef<HTMLDivElement>(null);
  const mapInstance = useRef<naver.maps.Map | null>(null);
  const { loaded: scriptLoaded, error: scriptError } = useScript(
    `https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${
      import.meta.env.VITE_NAVER_CLIENT_ID
    }`
  );

  // 맵 초기화: 스크립트 로드 후 한 번만 생성
  useEffect(() => {
    if (!scriptLoaded || !mapRef.current) return;

    if (!mapInstance.current) {
      const initialPosition = routeData.routePoint?.[0]
        ? new naver.maps.LatLng(
            routeData.routePoint[0].latitude,
            routeData.routePoint[0].longitude
          )
        : new naver.maps.LatLng(DEFAULT_COORD.lat, DEFAULT_COORD.lng);

      mapInstance.current = new naver.maps.Map(mapRef.current, {
        center: initialPosition,
        zoom: 12,
      });
    }
  }, [scriptLoaded, routeData]);

  // 경로 데이터 변경 시 폴리라인 갱신 및 지도 bounds 재설정
  useEffect(() => {
    if (!scriptLoaded || !mapInstance.current) return;
    if (!routeData.routePoint || !routeData.routePoint.length) return;

    // 경로 좌표 생성
    const path = routeData.routePoint.map(
      (point) => new naver.maps.LatLng(point.latitude, point.longitude)
    );

    // 기존 폴리라인 제거 후 새로 생성
    let polyline: naver.maps.Polyline | null = new naver.maps.Polyline({
      map: mapInstance.current,
      path,
      strokeColor: "#ff2a2a",
      strokeOpacity: 0.8,
      strokeWeight: 4,
    });

    // 경로에 맞춰 지도 영역 재설정
    const bounds = new naver.maps.LatLngBounds();
    path.forEach((latlng) => bounds.extend(latlng));
    mapInstance.current.fitBounds(bounds);

    // 맵이 idle 상태가 되면 다시 fitBounds를 호출해 초기 렌더링 문제를 보완
    (naver.maps.Event as any).once(mapInstance.current, "idle", () => {
      mapInstance.current!.fitBounds(bounds);
    });

    return () => {
      (polyline as any)?.setMap(null);
      polyline = null;
    };
  }, [scriptLoaded, routeData]);

  if (scriptError) {
    return <div>네이버 지도를 불러오지 못했습니다.</div>;
  }

  return <div ref={mapRef} style={{ width: "500px", height: "300px" }} />;
};

export default NaverMap;
