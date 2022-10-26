package com.mcarrental.carservice.service;

import com.mcarrental.carservice.event.CarCreateEvent;
import com.mcarrental.carservice.event.CarPriceUpdateEvent;
import com.mcarrental.carservice.event.CarUpdateEvent;

public interface CarEventSender {

    void sendCarCreateEvent(CarCreateEvent event);

    void sendCarUpdateEvent(CarUpdateEvent event);

    void sendCarPriceUpdateEvent(CarPriceUpdateEvent event);
}