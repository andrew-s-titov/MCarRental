package com.mcarrental.carservice.exception;

import com.mcarrental.carservice.util.RestUtil;

public class InsufficientRightsException extends RuntimeException {

    public InsufficientRightsException(String message) {
        super(message);
    }

    public InsufficientRightsException() {
        super(RestUtil.ACCESS_DENIED_MESSAGE);
    }
}