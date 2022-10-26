package com.mcarrental.mailservice.service.impl;

import com.mcarrental.mailservice.dto.MailDTO;
import com.mcarrental.mailservice.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class MailSenderBase implements MailSender {

    private static final String TEMPLATE_NAME = "email.html";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${mail.from}")
    private String from;
    @Value("${mail.app-name}")
    private String appName;
    @Value("${mail.template.arg.message-parts}")
    private String messagePartsArgName;
    @Value("${mail.template.arg.links}")
    private String linksArgName;
    @Value("${mail.template.arg.logo}")
    private String logosArgName;

    @Override
    public void sendEmail(MailDTO mail) {
        try {
            var mimeMessage = createMimeMessage(mail);
            this.mailSender.send(mimeMessage);

            log.info("An email was sent for " + mail.getTo());
        } catch (MessagingException ex) {
            log.error("An error occurred during email sending", ex);
        }
    }

    private MimeMessage createMimeMessage(MailDTO mail) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var mimeMessageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        mimeMessageHelper.setFrom(from);
        defineAddressee(mimeMessageHelper, mail);
        mimeMessageHelper.setText(createHtmlContent(mail), true);
        mimeMessageHelper.setSubject(appName + ": " + mail.getSubject());
        mimeMessageHelper.addInline(logosArgName, new ClassPathResource("/static/image/logo.png"));
        return message;
    }

    private String createHtmlContent(MailDTO mail) {
        Context templateContext = new Context();
        defineContextLocale(templateContext, mail);
        templateContext.setVariable(messagePartsArgName, mail.getMessageParts());
        templateContext.setVariable(linksArgName, mail.getLinks());
        templateContext.setVariable(logosArgName, logosArgName);
        return templateEngine.process(TEMPLATE_NAME, templateContext);
    }

    private void defineContextLocale(Context context, MailDTO mail) {
        Locale locale = mail.getLocale();
        if (locale != null) {
            context.setLocale(locale);
            log.debug("Locale set to context: " + locale);
        }
        log.debug("Falling back to default locale due to empty Locale passed");
    }
    
    abstract void defineAddressee(MimeMessageHelper mimeMessageHelper, MailDTO mail) throws MessagingException;
}