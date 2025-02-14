package com.taiso.bike_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.exception.RouteNotFoundException;
import com.taiso.bike_api.repository.RouteRepository;

@Service
public class RouteDeleteService {

    @Autowired
    private RouteRepository routeRepository;

    public void deleteRoute(Long routeId) {
        // routeId 유효성 검사
        if (routeId == null || routeId <= 0) {
            throw new IllegalArgumentException(routeId + " 값은 올바르지 않음");
        }

        // routeId에 해당하는 RouteEntity 조회
        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException(routeId + "번 루트를 찾을 수 없음"));
       
        // 대상 루트 삭제
        routeRepository.delete(routeEntity);
    }

}
