package com.mcarrental.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordResetEvent {

    private final String email;

    private final String code;
}
