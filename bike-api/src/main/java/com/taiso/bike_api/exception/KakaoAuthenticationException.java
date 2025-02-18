package com.taiso.bike_api.exception;

public class KakaoAuthenticationException extends RuntimeException {
    public KakaoAuthenticationException(String message) {
        super(message);
    }

    public KakaoAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 