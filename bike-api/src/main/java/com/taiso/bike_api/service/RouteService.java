package com.taiso.bike_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.RouteLikeEntity;
import com.taiso.bike_api.domain.RoutePointEntity;
import com.taiso.bike_api.domain.RouteTagCategoryEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.dto.RouteDetailResponseDTO;
import com.taiso.bike_api.dto.RouteListResponseDTO;
import com.taiso.bike_api.dto.RoutePointDTO;
import com.taiso.bike_api.dto.RouteResponseDTO;
import com.taiso.bike_api.exception.RouteDeleteAccessDeniedException;
import com.taiso.bike_api.exception.RouteLikeAlreadyExistsException;
import com.taiso.bike_api.exception.RouteLikeNotFoundException;
import com.taiso.bike_api.exception.RouteNotFoundException;
import com.taiso.bike_api.exception.UserNotFoundException;
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


    @Transactional
	public void postRouteLike(Authentication authentication, Long routeId) {
    	// 사용자와 루트를 한 번만 조회
    	UserEntity user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    	RouteEntity route = routeRepository.findById(routeId)
            .orElseThrow(() -> new RouteNotFoundException("루트를 찾을 수 없습니다."));
    
    	// 이미 좋아요한 경우 예외 발생
   	 	if (routeLikeRepository.existsByUser_UserIdAndRoute_RouteId(user.getUserId(), routeId)) {
        	throw new RouteLikeAlreadyExistsException("이미 해당 루트를 좋아요했습니다.");
    	}
    
    	// 좋아요 Entity 생성 및 저장
    	RouteLikeEntity routeLike = RouteLikeEntity.toEntity(route, user);
    	try {
        	routeLikeRepository.save(routeLike);
        	route.setLikeCount(route.getLikeCount() + 1);
    	} catch (Exception e) {
        	throw new RuntimeException("좋아요 등록 실패", e);
    	}
	}

	@Transactional
	public void deleteRouteLike(Authentication authentication, Long routeId) {
    	// 사용자와 루트를 한 번만 조회
    	UserEntity user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    	RouteEntity route = routeRepository.findById(routeId)
            .orElseThrow(() -> new RouteNotFoundException("루트를 찾을 수 없습니다."));
    
    	// 해당 좋아요가 존재하지 않으면 예외 발생
    	RouteLikeEntity routeLike = routeLikeRepository.findByUser_UserIdAndRoute_RouteId(user.getUserId(), routeId)
            .orElseThrow(() -> new RouteLikeNotFoundException("해당 루트에 대한 좋아요가 존재하지 않습니다."));
    
		// 좋아요 삭제 및 좋아요 수 감소
		try {
			routeLikeRepository.delete(routeLike);
			route.setLikeCount(route.getLikeCount() - 1);
		} catch (Exception e) {
			throw new RuntimeException("좋아요 삭제 실패", e);
		}
	}


	// 루트 리스트 조회 기능
	public RouteListResponseDTO getRouteList(int page, int size) {
		// 정의된 페이징 박스
		Pageable pageable = PageRequest.of(page, size);
		// route를 모두 불러옴
		Page<RouteEntity> routePage = routeRepository.findAll(pageable);

		log.info("repository에서 담아온 데이터 : {}", routePage);

		// map()을 이용해 각 Entity를 DTO로 변환
		List<RouteResponseDTO> routeResponseDTO = routePage.getContent().stream()
				.map(route -> new RouteResponseDTO(
						route.getRouteId(),
						route.getRouteImgId(),
						route.getUserId(),
						route.getRouteName(),
						route.getDescription(),
						route.getLikeCount(),
						route.getTags().stream()
								.map(RouteTagCategoryEntity::getName) // `tagName`을 추출해서 List<String>으로 변환
								.collect(Collectors.toList()), // 선택적 필드 (null일 수 있음)
						route.getDistance().floatValue(), // 선택적 필드 (null일 수 있음)
						route.getAltitude().floatValue(), // 선택적 필드 (null일 수 있음)
						route.getDistanceType().toString(), // 선택적 필드 (null일 수 있음)
						route.getAltitudeType().toString(), // 선택적 필드 (null일 수 있음)
						route.getRoadType().toString(), // 선택적 필드 (null일 수 있음)
						route.getCreatedAt() != null ? route.getCreatedAt().toString() : null, // 날짜 포맷 (null 체크)
						route.getFileName()
				//						route.getFileType().toString()			// 선택적 필드 (null일 수 있음)
				))
				.collect(Collectors.toList());

		log.info("서비스에서 나가기 직전 리스트 : {}", routeResponseDTO);

		return RouteListResponseDTO.builder()
				.content(routeResponseDTO)
				.pageNo(routePage.getNumber() + 1) // 페이지 번호는 1부터 시작하는 것이 일반적
				.pageSize(routePage.getSize())
				.totalElements(routePage.getTotalElements())
				.totalPages(routePage.getTotalPages())
				.last(routePage.isLast())
				.build();
	}
	
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
