package com.mcarrental.userservice.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class AuthenticationErrorException extends RuntimeException {

    private final Map<String, Object> details;

    public AuthenticationErrorException(String message) {
        super(message);
        this.details = null;
    }

    public AuthenticationErrorException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }
}
