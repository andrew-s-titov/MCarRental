package com.mcarrental.userservice.exception;

import com.mcarrental.userservice.util.RestUtil;

public class InsufficientRightsException extends RuntimeException {

    public InsufficientRightsException(String message) {
        super(message);
    }

    public InsufficientRightsException() {
        super(RestUtil.ACCESS_DENIED_MESSAGE);
    }
}