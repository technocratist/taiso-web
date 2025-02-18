package com.taiso.bike_api.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.LoginRequestDTO;
import com.taiso.bike_api.dto.LoginResponseDTO;
import com.taiso.bike_api.dto.RegisterRequestDTO;
import com.taiso.bike_api.dto.RegisterResponseDTO;
import com.taiso.bike_api.exception.KakaoAuthenticationException;
import com.taiso.bike_api.security.JwtTokenProvider;
import com.taiso.bike_api.service.AuthService;
import com.taiso.bike_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@RequestMapping("/api/auth")
@Tag(name = "인증 컨트롤러", description = "인증 관련 API")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    // 주입된 MemberService를 통해 회원가입을 처리
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증 및 JWT 토큰 발급")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        log.info("loginRequestDTO: {}", loginRequestDTO);
        
        // 사용자 인증 수행
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
            
        
        String jwt = jwtTokenProvider.generateToken(authentication.getName());
    
        // JWT를 HttpOnly, Secure 쿠키에 저장 (환경에 따라 secure 옵션은 개발 시 false로 설정할 수 있음)
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);      // 자바스크립트에서 접근 불가능
        jwtCookie.setSecure(true);        // HTTPS 환경에서만 전송 (개발 환경이라면 false)
        jwtCookie.setPath("/");           // 모든 경로에서 쿠키 접근 허용
        jwtCookie.setMaxAge(60 * 10);       // 쿠키 유효기간 설정 (예: 1시간)
    
        // 응답 헤더에 쿠키 추가
        response.addCookie(jwtCookie);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUserEmail(authentication.getName());
        loginResponseDTO.setUserId(userService.getUserIdByEmail(authentication.getName()));

    
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
    }

    // 회원가입
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "사용자 회원가입 및 JWT 토큰 발급")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO, HttpServletResponse httpServletResponse) {
        // 회원가입 처리
        RegisterResponseDTO registerResponseDTO = userService.register(registerRequestDTO);



        // 가입된 사용자의 이메일을 기반으로 JWT 토큰 발급
        String jwt = jwtTokenProvider.generateToken(registerResponseDTO.getEmail());

        // JWT 토큰을 HttpOnly, Secure 쿠키에 저장
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);      // 자바스크립트에서 접근 불가
        jwtCookie.setSecure(true);        // HTTPS 환경에서만 전송 (개발 환경에서는 옵션 변경 가능)
        jwtCookie.setPath("/");           // 모든 경로에서 쿠키 접근 허용
        jwtCookie.setMaxAge(60 * 60);      // 쿠키 유효기간 (예: 1시간)

        httpServletResponse.addCookie(jwtCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDTO);
    }

    // 로그아웃
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "쿠키 삭제를 통한 사용자 로그아웃")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // 클라이언트 측 JWT 쿠키 삭제
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true); // 자바스크립트 접근 불가
        jwtCookie.setSecure(true); // HTTPS 환경에서만 전송
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키 접근 허용
        jwtCookie.setMaxAge(0); // 0초: 즉시 삭제하도록 설정
        response.addCookie(jwtCookie);

        log.info("User logged out: JWT cookie cleared");
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
    
    /**
     * 프론트엔드에서 인가 코드(code)를 POST로 전달하면
     * 카카오 인증 및 JWT 발급을 수행하고 JWT를 반환함.
     */
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body, HttpServletResponse response) {
        String code = body.get("code");
        if (code == null) {
            return ResponseEntity.badRequest().body("Missing code parameter");
        }
        try {
            // processKakaoLogin 메서드로 토큰 생성
            String jwtToken = authService.processKakaoLogin(code);
            // JWT를 HttpOnly, Secure 쿠키에 저장 (코드 내 다른 엔드포인트와 동일한 쿠키 옵션)
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);      
            jwtCookie.setSecure(true);        
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 10); // 예: 10분 유효
            response.addCookie(jwtCookie);

            // 성공 메시지를 반환 (토큰은 쿠키에 저장됨)
            Map<String, String> result = new HashMap<>();
            result.put("message", "Kakao login successful");
            return ResponseEntity.ok(result);
        } catch (KakaoAuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }


}
