package com.mcarrental.bookingservice.service.impl;

import com.mcarrental.bookingservice.event.BookingAbortEvent;
import com.mcarrental.bookingservice.event.BookingApproveEvent;
import com.mcarrental.bookingservice.event.BookingCreateEvent;
import com.mcarrental.bookingservice.event.BookingDeclineEvent;
import com.mcarrental.bookingservice.event.BookingEvent;
import com.mcarrental.bookingservice.event.LocalizedEvent;
import com.mcarrental.bookingservice.event.completion.BookingDamagedCompletionEvent;
import com.mcarrental.bookingservice.event.completion.BookingDelayedCompletionEvent;
import com.mcarrental.bookingservice.event.completion.BookingNormalCompletionEvent;
import com.mcarrental.bookingservice.service.BookingEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaBookingEventSenderImpl implements BookingEventSender {

    @Value("${kafka.topic.booking.create}")
    private String kafkaBookingCreateTopic;
    @Value("${kafka.topic.booking.approve}")
    private String kafkaBookingApproveTopic;
    @Value("${kafka.topic.booking.decline}")
    private String kafkaBookingDeclineTopic;
    @Value("${kafka.topic.booking.abort}")
    private String kafkaBookingAbortTopic;
    @Value("${kafka.topic.booking.complete.normal}")
    private String kafkaBookingNormalCompletionTopic;
    @Value("${kafka.topic.booking.complete.delayed}")
    private String kafkaBookingDelayedCompletionTopic;
    @Value("${kafka.topic.booking.complete.damaged}")
    private String kafkaBookingDamagedCompletionTopic;

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @TransactionalEventListener
    @Override
    public void sendCreateEvent(BookingCreateEvent event) {
        sendEventToKafkaLoggable(kafkaBookingCreateTopic, event, "Create");
    }

    @TransactionalEventListener
    @Override
    public void sendApproveEvent(BookingApproveEvent event) {
        sendLocalizedEventToKafkaLoggable(kafkaBookingApproveTopic, event, "Approve");
    }

    @TransactionalEventListener
    @Override
    public void sendDeclineEvent(BookingDeclineEvent event) {
        sendLocalizedEventToKafkaLoggable(kafkaBookingDeclineTopic, event, "Decline");
    }

    @TransactionalEventListener
    @Override
    public void sendAbortEvent(BookingAbortEvent event) {
        sendLocalizedEventToKafkaLoggable(kafkaBookingAbortTopic, event, "Abort");
    }

    @TransactionalEventListener
    @Override
    public void sendEarlyCompletionEvent(BookingNormalCompletionEvent event) {
        sendEventToKafkaLoggable(kafkaBookingNormalCompletionTopic, event, "Normal completion");
    }

    @TransactionalEventListener
    @Override
    public void sendDelayedCompletionEvent(BookingDelayedCompletionEvent event) {
        sendEventToKafkaLoggable(kafkaBookingDelayedCompletionTopic, event, "Delayed completion");
    }

    @TransactionalEventListener
    @Override
    public void sendDamagedCompletionEvent(BookingDamagedCompletionEvent event) {
        sendEventToKafkaLoggable(kafkaBookingDamagedCompletionTopic, event, "Damaged completion");
    }

    private <T extends BookingEvent> void sendEventToKafkaLoggable(String kafkaTopic, T event, String eventName) {
        kafkaTemplate.send(kafkaTopic, event);
        log.info("{} event for booking {} sent to Kafka", eventName, event.getBookingId());
    }

    private <T extends BookingEvent & LocalizedEvent> void sendLocalizedEventToKafkaLoggable(
            String kafkaTopic, T event, String eventName) {
        event.setLocale(LocaleContextHolder.getLocale());
        sendEventToKafkaLoggable(kafkaTopic, event, eventName);
    }
}