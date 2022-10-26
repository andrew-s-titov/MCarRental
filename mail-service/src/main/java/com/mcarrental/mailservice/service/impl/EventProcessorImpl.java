package com.mcarrental.mailservice.service.impl;

import com.mcarrental.mailservice.config.LocaleSupport;
import com.mcarrental.mailservice.dto.MailDTO;
import com.mcarrental.mailservice.enums.Role;
import com.mcarrental.mailservice.event.LocalizedEvent;
import com.mcarrental.mailservice.event.booking.BookingAbortEvent;
import com.mcarrental.mailservice.event.booking.BookingApproveEvent;
import com.mcarrental.mailservice.event.booking.BookingEvent;
import com.mcarrental.mailservice.event.user.AuthEvent;
import com.mcarrental.mailservice.service.CarServiceReactiveClient;
import com.mcarrental.mailservice.service.EventProcessor;
import com.mcarrental.mailservice.service.MailSender;
import com.mcarrental.mailservice.service.UserServiceReactiveClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class EventProcessorImpl implements EventProcessor {

    private final UserServiceReactiveClient userServiceClient;
    private final CarServiceReactiveClient carServiceClient;
    private final MailSender mailSender;
    private final MessageSource messageSource;

    @Value("${external.url.booking-service.booking-info}")
    private String bookingUrl;
    @Value("${security.email-verification.ttl-hours}")
    private String emailVerificationCodeTtlHours;
    @Value("${external.url.user-service.email-verification}")
    private String emailVerificationEndpoint;
    @Value("${security.reset-password.ttl-minutes}")
    private String resetPasswordTtlMinutes;
    @Value("${external.url.user-service.new-password}")
    private String newPasswordEndpoint;

    @Override
    @KafkaListener(topics = "${kafka.topic.booking.approve}", groupId = "${spring.kafka.consumer.group-id}")
    public void processBookingApprove(BookingApproveEvent event) {
        withWebClientErrorHandling(() -> {
            // running 3 async calls and waiting for all of them to return the result
            Tuple3<String, String, String> emailInfo = Mono.zip(
                            userServiceClient.getUserEmail(event.getClientId()),
                            userServiceClient.getUserEmail(event.getCarOwnerId()),
                            carServiceClient.getCarShortInfo(event.getCarId()))
                    .block();
            Assert.notNull(emailInfo, "null webclient call result (tuple)");

            var clientEmail = emailInfo.getT1();
            var ownerEmail = emailInfo.getT2();
            var carShortInfo = emailInfo.getT3();

            var emailLocale = resolveLocale(event);
            var emailSubject = messageSource.getMessage("booking.event.approved.subject", null, emailLocale);
            var clientMainMessage = messageSource.getMessage("booking.event.approved.client", new String[]{carShortInfo}, emailLocale);
            var ownerMainMessage = messageSource.getMessage("booking.event.approved.owner", new String[]{carShortInfo}, emailLocale);

            sendMailToClientAndCarOwner(event, emailSubject, emailLocale, clientEmail, clientMainMessage, ownerEmail, ownerMainMessage);
        });
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.booking.abort}", groupId = "${spring.kafka.consumer.group-id}")
    public void processBookingAbort(BookingAbortEvent event) {
        withWebClientErrorHandling(() -> {
            Tuple2<String, String> emailInfo = Mono.zip(
                            userServiceClient.getUserEmail(event.getClientId()),
                            userServiceClient.getUserEmail(event.getCarOwnerId()))
                    .block();
            Assert.notNull(emailInfo, "null webclient call result (tuple)");

            var clientEmail = emailInfo.getT1();
            var ownerEmail = emailInfo.getT2();

            var emailLocale = resolveLocale(event);
            var emailSubject = messageSource.getMessage("booking.event.aborted.subject", null, emailLocale);
            var clientMainMessage = abortedMessage(event, Role.CLIENT, emailLocale);
            var ownerMainMessage = abortedMessage(event, Role.CAR_OWNER, emailLocale);

            sendMailToClientAndCarOwner(event, emailSubject, emailLocale, clientEmail, clientMainMessage, ownerEmail, ownerMainMessage);
        });
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.user.email-verification}", groupId = "${spring.kafka.consumer.group-id}")
    public void processEmailVerification(AuthEvent event) {
        var messageParts = emailVerificationMessageParts(resolveLocale(event));
        var verifyEmailLink = UriComponentsBuilder.fromHttpUrl(emailVerificationEndpoint)
                .queryParam("code", event.getCode())
                .toUriString();
        var mail = createUserAuthMessage(event, "verification.subject", messageParts,
                "verification.link.description", verifyEmailLink);
        mailSender.sendEmail(mail);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.user.password-reset}", groupId = "${spring.kafka.consumer.group-id}")
    public void processPasswordReset(AuthEvent event) {
        var messageParts = passwordResetMessageParts(resolveLocale(event));
        var newPasswordLink = UriComponentsBuilder.fromHttpUrl(newPasswordEndpoint)
                .queryParam("code", event.getCode())
                .toUriString();
        var mail = createUserAuthMessage(event, "password-reset.subject", messageParts,
                "password-reset.link.description", newPasswordLink);
        mailSender.sendEmail(mail);
    }

    private void sendMailToClientAndCarOwner(BookingEvent event, String subject, Locale locale,
                                             String clientEmail, String clientMessage,
                                             String ownerEmail, String carOwnerMessage) {
        String bookingLinkPointer = messageSource.getMessage("booking.link.pointer", null, locale);
        List<String> clientMessageParts = List.of(clientMessage, bookingLinkPointer);
        List<String> ownerMessageParts = List.of(carOwnerMessage, bookingLinkPointer);
        String bookingLinkDescription = messageSource.getMessage("booking.link.description", null, locale);
        Map<String, String> links = Collections.singletonMap(bookingLinkDescription, bookingUrlPart(event.getBookingId()));

        mailSender.sendEmail(new MailDTO(clientEmail, subject, clientMessageParts, links, locale));
        mailSender.sendEmail(new MailDTO(ownerEmail, subject, ownerMessageParts, links, locale));
    }

    private String bookingUrlPart(UUID bookingId) {
        return bookingUrl + "/" + bookingId;
    }

    private String abortedMessage(BookingAbortEvent event, Role messageFor, Locale locale) {
        Role roleOfWhoAborted = event.getRole();
        if (roleOfWhoAborted.equals(messageFor)) {
            return messageSource.getMessage("booking.event.aborted.self", null, locale);
        }
        String localizedWhoAborted = null;
        switch (roleOfWhoAborted) {
            case CLIENT:
                localizedWhoAborted = messageSource.getMessage("role.client", null, locale);
                break;
            case CAR_OWNER:
                localizedWhoAborted = messageSource.getMessage("role.owner", null, locale);
                break;
            case ADMIN_MAIN:
            case ADMIN_MANAGER:
                localizedWhoAborted = messageSource.getMessage("role.admin", null, locale);
                break;
        }
        return localizedWhoAborted != null ?
                messageSource.getMessage("booking.event.aborted.other", new String[]{localizedWhoAborted}, locale) :
                messageSource.getMessage("booking.event.aborted.default", null, locale);
    }

    private List<String> emailVerificationMessageParts(Locale locale) {
        return List.of(
                messageSource.getMessage("verification.link.pointer", null, locale),
                messageSource.getMessage("verification.ttl", new String[]{emailVerificationCodeTtlHours}, locale),
                messageSource.getMessage("verification.ignore", null, locale)
        );
    }

    private List<String> passwordResetMessageParts(Locale locale) {
        return List.of(
                messageSource.getMessage("password-reset.message", null, locale),
                messageSource.getMessage("password-reset.link.pointer", null, locale),
                messageSource.getMessage("password-reset.ttl", new String[]{resetPasswordTtlMinutes}, locale),
                messageSource.getMessage("verification.ignore", null, locale)
        );
    }

    private MailDTO createUserAuthMessage(AuthEvent event, String subjectBundleCode, List<String> messageParts,
                                          String linkDescriptionBundleCode, String link) {
        var emailLocale = resolveLocale(event);
        var emailSubject = messageSource.getMessage(subjectBundleCode, null, emailLocale);
        var linkDescription = messageSource.getMessage(linkDescriptionBundleCode, null, emailLocale);
        var linkPart = Collections.singletonMap(linkDescription, link);
        return new MailDTO(event.getEmail(), emailSubject, messageParts, linkPart, emailLocale);
    }

    private Locale resolveLocale(LocalizedEvent event) {
        Locale locale = event.getLocale();
        if (locale == null) {
            return LocaleSupport.DEFAULT_LOCALE;
        }
        return locale;
    }

    private void withWebClientErrorHandling(Runnable processWithWebClientCall) {
        try {
            processWithWebClientCall.run();
        } catch (WebClientResponseException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if (HttpStatus.NOT_FOUND.equals(statusCode)) {
                log.warn("Data inconsistency detected: ", ex);
            }
            if (statusCode.is5xxServerError()) {
                log.error("Server error response after WebClient exchange with other services", ex);
            }
        } catch (WebClientException ex) {
            log.error("Unexpected result during WebClient exchange with other services", ex);
        }
    }
}