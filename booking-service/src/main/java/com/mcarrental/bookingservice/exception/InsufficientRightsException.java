package com.mcarrental.bookingservice.exception;

import com.mcarrental.bookingservice.util.RestUtil;

public class InsufficientRightsException extends RuntimeException {

    public InsufficientRightsException(String message) {
        super(message);
    }

    public InsufficientRightsException() {
        super(RestUtil.ACCESS_DENIED_MESSAGE);
    }
}