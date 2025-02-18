package com.taiso.bike_api.exception;

public class RouteDeleteAccessDeniedException extends RuntimeException {
    public RouteDeleteAccessDeniedException(String message) {
        super(message);
    }
}
