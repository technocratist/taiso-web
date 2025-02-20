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
  const polylineRef = useRef<naver.maps.Polyline | null>(null);
  const { loaded: scriptLoaded, error: scriptError } = useScript(
    `https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${
      import.meta.env.VITE_NAVER_CLIENT_ID
    }`
  );

  // 지도 초기화: 스크립트 로드 후 한 번만 생성
  useEffect(() => {
    if (!scriptLoaded || !mapRef.current) return;

    if (!mapInstance.current) {
      const initialPosition = routeData?.routePoint?.[0]
        ? new naver.maps.LatLng(
            routeData.routePoint[0].latitude,
            routeData.routePoint[0].longitude
          )
        : new naver.maps.LatLng(DEFAULT_COORD.lat, DEFAULT_COORD.lng);

      mapInstance.current = new naver.maps.Map(mapRef.current, {
        center: initialPosition,
        zoom: 12,
        zoomControl: true,
        zoomControlOptions: {
          style: naver.maps.ZoomControlStyle.SMALL,
          position: naver.maps.Position.RIGHT_CENTER,
        },
      });

      // 지도 로딩 완료 후 idle 이벤트에서 폴리라인 위치 재조정
      naver.maps.Event.addListener(mapInstance.current, "idle", () => {
        // routeData가 존재하고 폴리라인이 생성되어 있다면 bounds 재설정
        if (
          routeData &&
          routeData.routePoint &&
          routeData.routePoint.length &&
          polylineRef.current
        ) {
          const path = routeData.routePoint.map(
            (point) => new naver.maps.LatLng(point.latitude, point.longitude)
          );
          const bounds = new naver.maps.LatLngBounds();
          path.forEach((latlng) => bounds.extend(latlng));
          mapInstance.current!.fitBounds(bounds);
        }
      });
    }
  }, [scriptLoaded]);

  // 경로 데이터 변경 시 폴리라인 갱신 및 지도 bounds 재설정
  useEffect(() => {
    if (!scriptLoaded || !mapInstance.current) return;
    if (!routeData?.routePoint || routeData.routePoint.length === 0) return;

    // 경로 좌표 생성 및 bounds 계산
    const path = routeData.routePoint.map(
      (point) => new naver.maps.LatLng(point.latitude, point.longitude)
    );
    const bounds = new naver.maps.LatLngBounds();
    path.forEach((latlng) => bounds.extend(latlng));

    // 기존 폴리라인 제거 (이미 존재한다면)
    if (polylineRef.current) {
      polylineRef.current.setMap(null);
    }

    // 새 폴리라인 생성
    polylineRef.current = new naver.maps.Polyline({
      map: mapInstance.current,
      path,
      strokeColor: "#ff2a2a",
      strokeOpacity: 0.8,
      strokeWeight: 4,
    });

    // 즉시 bounds 설정
    mapInstance.current.fitBounds(bounds);

    // 짧은 지연 후 다시 bounds 설정하여 초기 렌더링 문제 보완
    setTimeout(() => {
      if (mapInstance.current) {
        mapInstance.current.fitBounds(bounds);
      }
    }, 100);

    // 컴포넌트 언마운트 또는 routeData 변경 시 폴리라인 제거
    return () => {
      if (polylineRef.current) {
        polylineRef.current.setMap(null);
        polylineRef.current = null;
      }
    };
  }, [scriptLoaded, routeData]);

  if (scriptError) {
    return <div>네이버 지도를 불러오지 못했습니다.</div>;
  }

  return <div ref={mapRef} style={{ width: "500px", height: "300px" }} />;
};

export default NaverMap;
