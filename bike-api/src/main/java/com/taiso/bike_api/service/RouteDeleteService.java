package com.taiso.bike_api.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.exception.RouteDeleteAccessDeniedException;
import com.taiso.bike_api.exception.RouteNotFoundException;
import com.taiso.bike_api.exception.UserNotFoundException;
import com.taiso.bike_api.repository.RouteRepository;
import com.taiso.bike_api.repository.UserRepository;

@Service
public class RouteDeleteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    public void deleteRoute(Long routeId, String userEmail) {
        
        // 루트 정보 조회
        RouteEntity routeEntity = routeRepository.findById(routeId)
        .orElseThrow(() -> new RouteNotFoundException(routeId + "번 루트를 찾을 수 없음"));

        // 루트를 만든 유저 정보 가져오기
        UserEntity userEntity = userRepository.findById(routeEntity.getUserId())
        .orElseThrow(() -> new UserNotFoundException("해당 루트를 만든 사용자를 찾을 수 없음"));

        // 루트 만든 사람과 삭제 요청한 사람이 일치하는지 검증
        if (!userEmail.equals(userEntity.getEmail())) {
            throw new RouteDeleteAccessDeniedException("삭제 권한이 없습니다.");
        }

        // 대상 루트 삭제
        routeRepository.delete(routeEntity);

    }
}
