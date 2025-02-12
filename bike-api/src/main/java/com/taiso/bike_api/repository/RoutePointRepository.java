package com.taiso.bike_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.RoutePointEntity;

public interface RoutePointRepository extends JpaRepository<RoutePointEntity, Long> {
} 