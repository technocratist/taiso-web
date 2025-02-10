package com.taiso.bike_api.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiso.bike_api.dto.ErrorResponseDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    private final ObjectMapper objectMapper;

    // ObjectMapper를 DI받거나, Bean을 주입해 사용
    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 접근 거부 시 호출되는 메서드     
    @Override
    public void handle(
        // 요청 객체
        HttpServletRequest request,
        // 응답 객체
        HttpServletResponse response,
        // 접근 거부 예외
        AccessDeniedException accessDeniedException) 
            throws
            // 예외 처리
            IOException, ServletException {

        // 1. ErrorResponse 생성
        String requestUri = request.getRequestURI();
        ErrorResponseDTO errorResponse = ErrorResponseDTO.makeErrorResponse("접근이 거부되었습니다.", HttpStatus.FORBIDDEN, requestUri);

        // 2. JSON 변환
        String responseJson = objectMapper.writeValueAsString(errorResponse);

        // 3. HTTP 응답 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 4. response 바디에 JSON 쓰기
        response.getWriter().write(responseJson);
    }
}