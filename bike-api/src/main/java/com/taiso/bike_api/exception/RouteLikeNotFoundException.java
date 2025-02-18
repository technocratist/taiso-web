package com.taiso.bike_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RouteLikeNotFoundException extends RuntimeException  {
    public RouteLikeNotFoundException(String message) {
        super(message);
    }
}
