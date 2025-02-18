package com.taiso.bike_api.exception;

public class RouteLikeAlreadyExistsException extends RuntimeException  {
    public RouteLikeAlreadyExistsException(String message) {
        super(message);
    }

}
