package com.taiso.bike_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoutePointDTO {
    private String route_point_id; // 경로 포인트의 고유 식별자 (필수)
    private Integer sequence;      // 경로상의 순서(1,2,3,...)
    private Float latitude;        // 위도
    private Float longitude;       // 경도
    private Float elevation;       // 해당 지점의 고도 (optional)
} 