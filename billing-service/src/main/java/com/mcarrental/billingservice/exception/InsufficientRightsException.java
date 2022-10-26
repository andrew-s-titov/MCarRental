package com.mcarrental.billingservice.exception;

import com.mcarrental.billingservice.util.RestUtil;

public class InsufficientRightsException extends RuntimeException {

    public InsufficientRightsException(String message) {
        super(message);
    }

    public InsufficientRightsException() {
        super(RestUtil.ACCESS_DENIED_MESSAGE);
    }
}