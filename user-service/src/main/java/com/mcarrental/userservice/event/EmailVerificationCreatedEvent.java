package com.mcarrental.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationCreatedEvent {

    private final String email;

    private final String code;
}