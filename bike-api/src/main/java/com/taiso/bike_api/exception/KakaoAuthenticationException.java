package com.taiso.bike_api.exception;

public class KakaoAuthenticationException extends RuntimeException {
    public KakaoAuthenticationException(String message) {
        super(message);
    }
} 