package com.mcarrental.mailservice.service.impl;

import com.mcarrental.mailservice.dto.MailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;

@Service
@Profile("dev")
public class MailSenderDevImpl extends MailSenderBase {

    @Value("${spring.mail.username}")
    private String devTo;

    public MailSenderDevImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    protected void defineAddressee(MimeMessageHelper mimeMessageHelper, MailDTO mail) throws MessagingException {
        mimeMessageHelper.setTo(devTo);
    }
}