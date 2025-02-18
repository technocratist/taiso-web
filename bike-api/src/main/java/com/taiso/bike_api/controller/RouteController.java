package com.taiso.bike_api.controller;

import com.taiso.bike_api.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.taiso.bike_api.domain.RouteEntity;
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
    @Operation(summary = "루트 삭제", description = "루트를 업로드한 유저가 루트를 삭제하는 API")
    public ResponseEntity<Void> deleteRoute(@PathVariable("routeId") Long routeId) {

        routeDeleteService.deleteRoute(routeId);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/")
    @Operation(summary = "루트 리스트 조회", description = "루트를 페이징하여 리스트로 불러오는 API")
    public ResponseEntity<RouteListResponseDTO> getUsers(
                                                    //필터정보 null 가능
                                                    @RequestParam(defaultValue = "") String sort,
                                                    @RequestParam(defaultValue = "") String distanceType,
                                                    @RequestParam(defaultValue = "") String altitudeType,
                                                    @RequestParam(defaultValue = "") String roadType,
                                                    @RequestParam(defaultValue = "") String[] Tag) {

        // 여기서 page, size의 변수값 수정으로 페이징 컨트롤 가능
        int page = 1;
        int size = 10;

        // 루트 데이터들을 페이징된 형태로 불러옴
        RouteListResponseDTO routeListResponseDTO = routeService.getRouteList(page, size);

        log.info("보내기 직전 : {}",routeListResponseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(routeListResponseDTO);
    }
}


