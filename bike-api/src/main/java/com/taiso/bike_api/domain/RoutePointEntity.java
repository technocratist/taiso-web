package com.taiso.bike_api.domain;



import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "route_point")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutePointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_point_id")
    private Long routePointId;

    /**
     * 해당 포인트가 속한 루트를 나타내는 외래키.
     * 다대일 관계로 Route 엔티티와 연결.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private RouteEntity route;

    /**
     * 해당 루트 내에서의 순서를 나타내는 값.
     */
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    /**
     * 위도 값. (예: 소수점 6자리까지 저장)
     */
    @Column(name = "latitude", nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    /**
     * 경도 값. (예: 소수점 6자리까지 저장)
     */
    @Column(name = "longitude", nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    /**
     * 고도 값. (예: 소수점 2자리까지 저장)
     */
    @Column(name = "elevation", precision = 10, scale = 2)
    private BigDecimal elevation;
}