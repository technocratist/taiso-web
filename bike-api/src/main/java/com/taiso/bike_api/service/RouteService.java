package com.taiso.bike_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.RouteLikeEntity;
import com.taiso.bike_api.domain.RoutePointEntity;
import com.taiso.bike_api.domain.RouteTagCategoryEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.dto.RouteDetailResponseDTO;
import com.taiso.bike_api.dto.RoutePointDTO;
import com.taiso.bike_api.exception.RouteNotFoundException;
import com.taiso.bike_api.repository.RouteLikeRepository;
import com.taiso.bike_api.repository.RoutePointRepository;
import com.taiso.bike_api.repository.RouteRepository;
import com.taiso.bike_api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class RouteService {
	
	@Autowired
	private RouteLikeRepository routeLikeRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RoutePointRepository routePointRepository;
    
    @Autowired
    private UserRepository userRepository;


    public RouteDetailResponseDTO getRouteById(Long routeId) {
        // routeId 유효성 검사 (0 이하인 경우 오류)
        if (routeId == null || routeId <= 0) {
            throw new IllegalArgumentException(routeId + " 값은 올바르지 않음");
        }

        // routeId에 해당하는 RouteEntity 조회
        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException(routeId + "번 루트를 찾을 수 없음"));

        // 해당 route의 포인트들을 sequence 순으로 조회
        List<RoutePointEntity> routePoints = routePointRepository.findByRouteOrderBySequenceAsc(routeEntity);
        List<RoutePointDTO> pointResponses = routePoints.stream()
                .map(rp -> RoutePointDTO.builder()
                        .route_point_id(rp.getRoutePointId().toString())
                        .sequence(rp.getSequence())
                        .latitude(rp.getLatitude().floatValue())
                        .longitude(rp.getLongitude().floatValue())
                        .elevation(rp.getElevation() != null ? rp.getElevation().floatValue() : null)
                        .build())
                .collect(Collectors.toList());

        // RouteEntity의 tag 필드는 Set<RouteTagCategoryEntity> 로 되어 있으므로,
        // 각 태그의 이름(예: getTagName())을 리스트로 변환 (해당 메서드는 RouteTagCategoryEntity에 구현되어 있다고 가정)
        List<String> tags = routeEntity.getTags().stream()
                .map(RouteTagCategoryEntity::getName)
                .collect(Collectors.toList());

        // DTO 빌드 (필드명이 spec과 동일하도록 변환)
        return RouteDetailResponseDTO.builder()
                .routeId(routeEntity.getRouteId())
                .routeImgId(routeEntity.getRouteImgId())
                .userId(routeEntity.getUserId())
                .routeName(routeEntity.getRouteName())
                .description(routeEntity.getDescription())
                .likeCount(routeEntity.getLikeCount())
                .tag(tags)
                .distance(routeEntity.getDistance().floatValue())
                .altitude(routeEntity.getAltitude().floatValue())
                .distanceType(routeEntity.getDistanceType().name())
                .altitudeType(routeEntity.getAltitudeType().name())
                .roadType(routeEntity.getRoadType().name())
                .createdAt(routeEntity.getCreatedAt().toString())
                .fileName(routeEntity.getFileName())
                .fileType(routeEntity.getFileType() != null ? routeEntity.getFileType().name() : null)
                .routePoint(pointResponses)
                .build();
    }


    // 루트 좋아요 등록 기능
    @Transactional
	public void save(Authentication authentication, Long routeId) {
    	
		// 유저 id 가져오기
    	Optional<UserEntity> userEntity = userRepository.findByEmail(authentication.getName());
		log.info("부모 값 userEntity: {}", userEntity.get());
		
		Optional<RouteEntity> routeEntity = routeRepository.findById(routeId);
		
		// 부모 글을 꺼냄
		UserEntity userEntity2 = userEntity.get();
		RouteEntity routeEntity2 = routeEntity.get();
		if(!routeEntity.isPresent()) return;
		if(!userEntity.isPresent()) return;
		
		// 부모글을 자식 entity에 전달
		RouteLikeEntity routeLikeEntity = RouteLikeEntity.toEntity(routeEntity2, userEntity2);
		
		// 저장
		routeLikeRepository.save(routeLikeEntity);
		
		routeEntity.get().setLikeCount( routeEntity.get().getLikeCount() +1 );
	}




    // 루트 좋아요 삭제 기능
    @Transactional
	public void delete(Long routeId, Authentication authentication) {

		// 루트 id 가져오기 -> like count 줄이기 
		Optional<RouteEntity> routeEntity = routeRepository.findById(routeId);
		
		// 유저 id 가져오기 
    	Optional<UserEntity> userEntity = userRepository.findByEmail(authentication.getName());
    	
    	Long userId = userRepository.findByEmail(authentication.getName()).get().getUserId();
		
		// 유저 id로 RouteLikeId가져와서 삭제하기?
    	RouteLikeEntity routeLikeEntity = routeLikeRepository.findByUser_UserIdAndRoute_RouteId(userId, routeId).get();
		
	    // 좋아요 삭제
	    routeLikeRepository.delete(routeLikeEntity);
		
		// like count 1줄이기
		routeEntity.get().setLikeCount( routeEntity.get().getLikeCount() -1 );
	}




}
