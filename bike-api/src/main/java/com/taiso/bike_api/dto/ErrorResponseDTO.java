package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
//에러 응답 DTO
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public static ErrorResponseDTO makeErrorResponse(String message, HttpStatus status, String path) {
        return ErrorResponseDTO.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message)
            .path(path)
            .build();
    }
}
// 예시
// {
//   "timestamp": "2025-02-06T12:34:56.789+00:00", // 에러 발생 시각

//   "status": 400, // 상태코드
  
//   "error": "Bad Request", //에러
  
//   "message": "Validation failed for field 'email'", //에러 설명(직접 작성 가능)
  
//   "path": "/api/users" //에러 발생 루트
// }
