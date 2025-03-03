package com.taiso.bike_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taiso.bike_api.domain.LightningUserEntity.ParticipantStatus;
import com.taiso.bike_api.dto.PasswordUpdateRequestDTO;
import com.taiso.bike_api.dto.UserDetailRequestDTO;
import com.taiso.bike_api.dto.UserDetailResponseDTO;
import com.taiso.bike_api.dto.UserLightningsGetResponseDTO;
import com.taiso.bike_api.security.JwtTokenProvider;
import com.taiso.bike_api.service.UserDetailService;
import com.taiso.bike_api.service.UserService;
import com.taiso.bike_api.dto.UserLightningReviewResponseDTO;
import com.taiso.bike_api.service.UserReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/api/users")
@Tag(name = "회원 컨트롤러", description = "회원 정보 관련 API")
public class UserController {

    @Autowired
    private UserDetailService userDetailService;
    
    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PatchMapping("/me/details")
    @Operation(summary = "내 페이지 정보 수정", description = "상세 프로필 페이지 정보 수정")
    public ResponseEntity<UserDetailResponseDTO> updateUserDetail(@RequestPart(value = "userDetailData") UserDetailRequestDTO userDetailRequestDTO
                                                                , @RequestPart(value = "profileImg", required = false) MultipartFile profileImg
                                                                , @RequestPart(value = "backgroundImg", required = false) MultipartFile backgroundImg
                                                                , HttpServletResponse httpServletResponse){

        log.info("로직 시작 : {}", userDetailRequestDTO.toString());
        log.info("profileImg : {}",profileImg.getOriginalFilename());
        log.info("backgroundImg : {}",backgroundImg.getOriginalFilename());

        // 파일Id를 포함한 DTO를 DB로 보내 저장
        userDetailService.updateUserDetail(userDetailRequestDTO, profileImg, backgroundImg);

        //return ResponseEntity.status(201).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "내 페이지 정보 조회", description = "회원 프로필 페이지 정보 조회")
    public ResponseEntity<UserDetailResponseDTO> getUserDetail(@PathVariable Long userId) {

        log.info(userId.toString());
        // 찾아온 데이터를 담기
        UserDetailResponseDTO userDetailResponseDTO = userDetailService.getUserDetailById(userId);
        log.info(userDetailResponseDTO.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailResponseDTO);
    }

    @GetMapping("/me/lightnings")
    public ResponseEntity<List<UserLightningsGetResponseDTO>> getUserLightnings(
        @RequestParam(name = "status") List<ParticipantStatus> status
        , @AuthenticationPrincipal String userEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserLightnings(status, userEmail));
    }
    
    
    // 리뷰 목록 출력 - 내가 작성한 회원 리뷰 조회
    @GetMapping("/lightnings/reviews")
    @Operation(summary = "내 페이지 정보 조회", description = "회원 프로필 페이지 정보 조회")
//    public void reviews(
    public ResponseEntity<List<UserLightningReviewResponseDTO>> reviews(
			Authentication authentication
    		) {
    	List<UserLightningReviewResponseDTO> response = userReviewService.myLightningReviews(authentication);

    	
//    	log.info("Controller response == {}",response);
    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 내 회원정보수정(비밀번호 수정)
    @PatchMapping("/me")
    @Operation(summary = "내 비밀번호 수정", description = "내 비밀번호 수정")
    public ResponseEntity<Void> updatePassword(
        @RequestBody PasswordUpdateRequestDTO requestDTO
        , @AuthenticationPrincipal String userEmail
        , HttpServletResponse response) {

        userService.updatePassword(requestDTO, userEmail);

        String jwt = jwtTokenProvider.generateToken(userEmail);
    
        // JWT를 HttpOnly, Secure 쿠키에 저장 (환경에 따라 secure 옵션은 개발 시 false로 설정할 수 있음)
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);      // 자바스크립트에서 접근 불가능
        jwtCookie.setSecure(true);        // HTTPS 환경에서만 전송 (개발 환경이라면 false)
        jwtCookie.setPath("/");           // 모든 경로에서 쿠키 접근 허용
        jwtCookie.setMaxAge(60 * 10);       // 쿠키 유효기간 설정 (예: 1시간)
    
        // 응답 헤더에 쿠키 추가
        response.addCookie(jwtCookie);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
