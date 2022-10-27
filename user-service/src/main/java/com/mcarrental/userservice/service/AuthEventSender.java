package com.mcarrental.userservice.service;

import com.mcarrental.userservice.event.EmailVerificationCreatedEvent;
import com.mcarrental.userservice.event.PasswordResetEvent;

public interface AuthEventSender {

    void sendVerificationMail(EmailVerificationCreatedEvent event);

    void sendPasswordResetMail(PasswordResetEvent event);
}