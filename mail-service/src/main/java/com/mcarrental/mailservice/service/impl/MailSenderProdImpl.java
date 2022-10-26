package com.mcarrental.mailservice.service.impl;

import com.mcarrental.mailservice.dto.MailDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;

@Service
@Profile("prod")
public class MailSenderProdImpl extends MailSenderBase {

    public MailSenderProdImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    protected void defineAddressee(MimeMessageHelper mimeMessageHelper, MailDTO mail) throws MessagingException {
        mimeMessageHelper.setTo(mail.getTo());
    }
}