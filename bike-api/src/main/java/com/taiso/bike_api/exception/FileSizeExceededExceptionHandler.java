package com.taiso.bike_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


// 파일 크기 초과 예외 처리
@RestControllerAdvice
public class FileSizeExceededExceptionHandler {

    // 파일 크기 초과 시 발생하는 예외를 처리하는 핸들러
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        String errorMessage = "업로드 파일의 크기가 제한을 초과하였습니다";
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorMessage);
    }
} 