package com.taiso.bike_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


import com.taiso.bike_api.dto.LoginRequestDTO;
import com.taiso.bike_api.dto.LoginResponseDTO;
import com.taiso.bike_api.dto.RegisterRequestDTO;
import com.taiso.bike_api.dto.RegisterResponseDTO;
import com.taiso.bike_api.security.JwtTokenProvider;
import com.taiso.bike_api.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    // 주입된 MemberService를 통해 회원가입을 처리
    @Autowired
    private MemberService memberService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        log.info("loginRequestDTO: {}", loginRequestDTO);
        
        // 사용자 인증 수행
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
    
        System.out.println("authentication: " + authentication.getName());
        
        String jwt = jwtTokenProvider.generateToken(authentication.getName());
    
        // JWT를 HttpOnly, Secure 쿠키에 저장 (환경에 따라 secure 옵션은 개발 시 false로 설정할 수 있음)
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);      // 자바스크립트에서 접근 불가능
        jwtCookie.setSecure(true);        // HTTPS 환경에서만 전송 (개발 환경이라면 false)
        jwtCookie.setPath("/");           // 모든 경로에서 쿠키 접근 허용
        jwtCookie.setMaxAge(60 * 60);       // 쿠키 유효기간 설정 (예: 1시간)
    
        // 응답 헤더에 쿠키 추가
        response.addCookie(jwtCookie);
    
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO, HttpServletResponse httpServletResponse) {
        // 회원가입 처리
        RegisterResponseDTO registerResponseDTO = memberService.register(registerRequestDTO);

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
}
