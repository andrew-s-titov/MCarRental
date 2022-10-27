package com.mcarrental.userservice.service.impl;

import com.mcarrental.userservice.event.EmailVerificationCreatedEvent;
import com.mcarrental.userservice.event.PasswordResetEvent;
import com.mcarrental.userservice.service.AuthEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaAuthEventSenderImpl implements AuthEventSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.user.email-verification}")
    private String emailVerificationKafkaTopic;
    @Value("${kafka.topic.user.password-reset}")
    private String resetPasswordKafkaTopic;

    @TransactionalEventListener
    @Override
    public void sendVerificationMail(EmailVerificationCreatedEvent event) {
        kafkaTemplate.send(emailVerificationKafkaTopic, event);
        log.info("Email verification event sent to Kafka for user with email {}", event.getEmail());
    }

    @TransactionalEventListener
    @Override
    public void sendPasswordResetMail(PasswordResetEvent event) {
        kafkaTemplate.send(resetPasswordKafkaTopic, event);
        log.info("Password reset event sent to Kafka for user with email {}", event.getEmail());
    }
}