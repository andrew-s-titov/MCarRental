package com.mcarrental.mailservice.service;

import com.mcarrental.mailservice.dto.MailDTO;

public interface MailSender {

    void sendEmail(MailDTO mail);
}