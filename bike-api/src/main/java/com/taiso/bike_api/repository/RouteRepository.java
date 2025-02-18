package com.taiso.bike_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.RouteEntity;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {
    //루트를 페이징처리하여 가져오기위한 메소드
    //Page는 JPA가 제공, Pageble은 Page의 형태를 정의
    Page<RouteEntity> findAll(Pageable pageable);


}
