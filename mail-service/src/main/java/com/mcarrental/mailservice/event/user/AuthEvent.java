package com.mcarrental.mailservice.event.user;

import com.mcarrental.mailservice.event.LocalizedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthEvent implements LocalizedEvent {

    private String email;

    private String code;

    private Locale locale;
}