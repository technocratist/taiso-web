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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private RouteEntity route;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "elevation", precision = 10, scale = 2)
    private BigDecimal elevation;
}