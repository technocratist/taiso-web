import React, { useEffect, useRef, useState } from "react";

// kakao 전역 객체 타입 선언 (타입 정의가 필요하다면 추가 작업)
declare global {
  interface Window {
    kakao: any;
  }
}

interface LatLng {
  lat: number;
  lng: number;
}

const MeetingLocationSelector: React.FC = () => {
  const mapRef = useRef<HTMLDivElement>(null);
  const [map, setMap] = useState<any>(null);
  // 마커 상태 대신 useRef를 사용하여 mutable하게 관리합니다.
  const markerRef = useRef<any>(null);
  const [inputAddress, setInputAddress] = useState<string>("");
  const [selectedAddress, setSelectedAddress] = useState<string>("");
  const [selectedCoords, setSelectedCoords] = useState<LatLng | null>(null);

  useEffect(() => {
    if (window.kakao && mapRef.current) {
      const container = mapRef.current;
      const options = {
        center: new window.kakao.maps.LatLng(37.553614, 126.878814), // 초기 지도 중심 좌표
        level: 10,
      };
      const mapInstance = new window.kakao.maps.Map(container, options);

      // 지도 타입 컨트롤 생성 및 지도에 추가 (오른쪽 상단)
      const mapTypeControl = new window.kakao.maps.MapTypeControl();
      mapInstance.addControl(
        mapTypeControl,
        window.kakao.maps.ControlPosition.TOPRIGHT
      );

      // 줌 컨트롤 생성 및 지도에 추가 (오른쪽)
      const zoomControl = new window.kakao.maps.ZoomControl();
      mapInstance.addControl(
        zoomControl,
        window.kakao.maps.ControlPosition.RIGHT
      );

      setMap(mapInstance);

      // 지도 클릭 시 좌표 선택 및 주소 변환 (법정동 주소)
      window.kakao.maps.event.addListener(
        mapInstance,
        "click",
        function (mouseEvent: any) {
          const clickedLatLng = mouseEvent.latLng;
          setSelectedCoords({
            lat: clickedLatLng.getLat(),
            lng: clickedLatLng.getLng(),
          });

          // 이미 마커가 있다면 위치만 업데이트, 없으면 새로 생성
          if (markerRef.current) {
            markerRef.current.setPosition(clickedLatLng);
          } else {
            markerRef.current = new window.kakao.maps.Marker({
              position: clickedLatLng,
              map: mapInstance,
            });
          }

          // 좌표로 주소 검색 (법정동 상세 주소)
          const geocoder = new window.kakao.maps.services.Geocoder();
          geocoder.coord2Address(
            clickedLatLng.getLng(),
            clickedLatLng.getLat(),
            function (result: any, status: any) {
              if (
                status === window.kakao.maps.services.Status.OK &&
                result[0]
              ) {
                setSelectedAddress(result[0].address.address_name);
              }
            }
          );
        }
      );
    }
  }, []); // 컴포넌트가 마운트될 때 한 번만 실행

  // 입력한 주소로 좌표 검색 및 마커 표시
  const handleAddressSearch = () => {
    if (!inputAddress || !map) return;

    const geocoder = new window.kakao.maps.services.Geocoder();
    geocoder.addressSearch(inputAddress, function (result: any, status: any) {
      if (status === window.kakao.maps.services.Status.OK && result[0]) {
        const lat = parseFloat(result[0].y);
        const lng = parseFloat(result[0].x);
        const coords = new window.kakao.maps.LatLng(lat, lng);
        map.setCenter(coords);
        setSelectedCoords({ lat, lng });
        setSelectedAddress(result[0].address_name);

        if (markerRef.current) {
          markerRef.current.setPosition(coords);
        } else {
          markerRef.current = new window.kakao.maps.Marker({
            map: map,
            position: coords,
          });
        }
      } else {
        alert("주소 결과가 없습니다. 정확한 주소를 입력해주세요.");
      }
    });
  };

  // 선택한 장소를 등록하는 함수 (백엔드 API 연동 등 추가 가능)
  const handleRegisterLocation = () => {
    if (selectedCoords && selectedAddress) {
      alert(
        `모임 시작 장소 등록: ${selectedAddress} (${selectedCoords.lat.toFixed(
          6
        )}, ${selectedCoords.lng.toFixed(6)})`
      );
    } else {
      alert("먼저 장소를 선택해주세요.");
    }
  };

  return (
    <div className="bg-base-100 rounded-lg ">
      <h2 className="text-2xl font-bold mb-4">모임 시작 장소 등록</h2>
      <div className="flex items-center mb-4">
        <input
          type="text"
          placeholder="주소를 입력하세요"
          value={inputAddress}
          onChange={(e) => setInputAddress(e.target.value)}
          className="input input-bordered w-[300px] mr-2"
        />
        <button onClick={handleAddressSearch} className="btn btn-primary">
          주소 검색
        </button>
      </div>
      {/* 지도 표시 영역 */}
      <div ref={mapRef} className="w-full h-[400px] mx-auto mb-4"></div>
      <div className="mt-4">
        <p className="mb-2">선택한 주소: {selectedAddress || "없음"}</p>
        <p className="mb-4">
          좌표:{" "}
          {selectedCoords
            ? `${selectedCoords.lat.toFixed(6)}, ${selectedCoords.lng.toFixed(
                6
              )}`
            : "없음"}
        </p>
        <button onClick={handleRegisterLocation} className="btn btn-secondary">
          장소 등록
        </button>
      </div>
    </div>
  );
};

export default MeetingLocationSelector;
