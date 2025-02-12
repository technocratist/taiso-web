package com.taiso.bike_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.RouteEntity;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {
    
}
