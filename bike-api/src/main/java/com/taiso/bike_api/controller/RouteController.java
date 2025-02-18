package com.taiso.bike_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.dto.RouteDetailResponseDTO;
import com.taiso.bike_api.dto.RouteLikePostResponseDTO;
import com.taiso.bike_api.dto.RoutePostRequestDTO;
import com.taiso.bike_api.dto.RoutePostResponseDTO;
import com.taiso.bike_api.exception.RouteLikeAlreadyExistsException;
import com.taiso.bike_api.exception.RouteLikeNotFoundException;
import com.taiso.bike_api.exception.RouteNotFoundException;
import com.taiso.bike_api.repository.RouteLikeRepository;
import com.taiso.bike_api.repository.RouteRepository;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.service.RouteCreateService;
import com.taiso.bike_api.service.RouteDeleteService;
import com.taiso.bike_api.service.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/routes")
@Tag(name = "루트 컨트롤러", description = "루트 관련 API")
public class RouteController {

    @Autowired
    private RouteService routeService;
    
    // 루트 좋아요 레파지토리
    @Autowired
    private RouteLikeRepository routeLikeRepository;

    // 루트 좋아요 레파지토리
    @Autowired
    private RouteRepository routeRepository;  
    // 루트 좋아요 레파지토리
    @Autowired
    private UserRepository userRepository;     
    
    
    @Autowired
    private RouteCreateService routeCreateService;

    @Autowired
    private RouteDeleteService routeDeleteService;

    @GetMapping("/{routeId}")
    @Operation(summary = "루트 디테일 조회", description = "루트 디테일 조회하는 API")
    public ResponseEntity<RouteDetailResponseDTO> getRoute(@PathVariable Long routeId) {
        RouteDetailResponseDTO route = routeService.getRouteById(routeId);
        return ResponseEntity.status(HttpStatus.OK).body(route);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "루트 생성", description = "루트를 생성하는 API")
    public ResponseEntity<RoutePostResponseDTO> createRoute(
            @RequestPart(value = "routeData") RoutePostRequestDTO routeData,
            @RequestPart(value = "file") MultipartFile file, Authentication authentication) {

        log.info("RouteController.createRoute() 호출됨");
        log.info("routeData: {}", routeData);
        log.info("file: {}", file);

        RoutePostResponseDTO response = routeCreateService.createRoute(routeData, file, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
	// 루트 좋아요 등록
    @PostMapping("/{routeId}/like")
    @Operation(summary = "루트 좋아요 등록", description = "루트 좋아요 생성 API")
    public ResponseEntity<RouteLikePostResponseDTO> createRouteLike(
    		@PathVariable(name = "routeId") Long routeId,
    		Authentication authentication
    		){
    	
    	
        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException("루트를 찾을 수 없습니다."));
    	

    	Long userId = userRepository.findByEmail(authentication.getName()).get().getUserId();
        boolean alreadyLiked = routeLikeRepository.existsByUser_UserIdAndRoute_RouteId(userId, routeId);
        if (alreadyLiked) {
            throw new RouteLikeNotFoundException("이미 해당 루트를 좋아요했습니다.");
        }   	
    	
    	

		// 좋아요 저장
		routeService.save(authentication, routeId);
    	// 정보 출력
    	return ResponseEntity.status(HttpStatus.CREATED).body(null);
	    	

    }

    
    //루트 좋아요 취소 기능
    @DeleteMapping("/{routeId}/like")
    @Operation(summary = "루트 좋아요 취소", description = "루트 좋아요 삭제 API")
    public ResponseEntity<RouteLikePostResponseDTO> DeleteRouteLike(
    		@PathVariable(name = "routeId") Long routeId,
    		Authentication authentication
    		) {

    	log.info("유저 authentication: {}", authentication.getName());
    	
    	// service -> 삭제 기능
    	routeService.delete(routeId, authentication);
    	
    	// 정보 출력
    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable("routeId") Long routeId) {

        routeDeleteService.deleteRoute(routeId);

        return ResponseEntity.noContent().build();

    }
}


