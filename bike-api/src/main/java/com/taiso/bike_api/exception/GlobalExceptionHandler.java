package com.taiso.bike_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.taiso.bike_api.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request) {
        log.error("RoleNotFoundException: ", ex);
            
        ErrorResponseDTO errorResponse = ErrorResponseDTO.makeErrorResponse("사용자 role을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStatusNotFoundException(StatusNotFoundException ex, HttpServletRequest request) {
        log.error("StatusNotFoundException: ", ex);
        ErrorResponseDTO errorResponse = ErrorResponseDTO.makeErrorResponse("사용자 status를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request) {
        log.error("EmailAlreadyExistsException: ", ex);
        ErrorResponseDTO errorResponse = ErrorResponseDTO.makeErrorResponse("이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        log.error("BadCredentialsException: ", ex);
        ErrorResponseDTO errorResponse = ErrorResponseDTO.makeErrorResponse("로그인 인증 정보가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(errorResponse); 
    }

    // 기타 예외 처리 (선택 사항)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: ", ex);
        ErrorResponseDTO errorResponse = ErrorResponseDTO.makeErrorResponse("서버에 문제가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
    
}