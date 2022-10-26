package com.mcarrental.carservice.service.impl;

import com.mcarrental.carservice.event.CarCreateEvent;
import com.mcarrental.carservice.event.CarEvent;
import com.mcarrental.carservice.event.CarPriceUpdateEvent;
import com.mcarrental.carservice.event.CarUpdateEvent;
import com.mcarrental.carservice.service.CarEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaCarEventSenderImpl implements CarEventSender {

    private final KafkaTemplate<String, CarEvent> kafkaTemplate;

    @Value("${kafka.topic.car.create}")
    private String carCreateTopic;
    @Value("${kafka.topic.car.update}")
    private String carUpdateTopic;
    @Value("${kafka.topic.car.update-price}")
    private String carPriceUpdateTopic;

    @TransactionalEventListener
    @Override
    public void sendCarCreateEvent(CarCreateEvent event) {
        kafkaTemplate.send(carCreateTopic, event);
        log.info("Create event sent to Kafka for car " + event.getCarId());
    }

    @TransactionalEventListener
    @Override
    public void sendCarUpdateEvent(CarUpdateEvent event) {
        kafkaTemplate.send(carUpdateTopic, event);
        log.info("Update event sent to Kafka for car " + event.getCarId());
    }

    @TransactionalEventListener
    @Override
    public void sendCarPriceUpdateEvent(CarPriceUpdateEvent event) {
        kafkaTemplate.send(carPriceUpdateTopic, event);
        log.info("Price update event sent to Kafka for car " + event.getCarId());
    }
}
