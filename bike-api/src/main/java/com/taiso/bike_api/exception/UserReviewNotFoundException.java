package com.taiso.bike_api.exception;

public class UserReviewNotFoundException extends RuntimeException {
    public UserReviewNotFoundException(String message) {
        super(message);
    }
}
