package com.mcarrental.carsearchservice.exception;

import com.mcarrental.carsearchservice.util.RestUtil;

public class InsufficientRightsException extends RuntimeException {

    public InsufficientRightsException(String message) {
        super(message);
    }

    public InsufficientRightsException() {
        super(RestUtil.ACCESS_DENIED_MESSAGE);
    }
}