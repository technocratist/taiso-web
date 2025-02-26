package com.taiso.bike_api.exception;

public class UserDetailNotFoundException extends RuntimeException {
    public UserDetailNotFoundException(String message) {
        super(message);
    }
}
