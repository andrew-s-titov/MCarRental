package com.mcarrental.carsearchservice.service;

import com.mcarrental.carsearchservice.event.BookingCancelEvent;
import com.mcarrental.carsearchservice.event.BookingCompletionEvent;
import com.mcarrental.carsearchservice.event.BookingCreateEvent;
import com.mcarrental.carsearchservice.event.CarCreateEvent;
import com.mcarrental.carsearchservice.event.CarPriceUpdateEvent;
import com.mcarrental.carsearchservice.event.CarUpdateEvent;

public interface CarSyncService {

    void createNewCar(CarCreateEvent event);

    void updateCar(CarUpdateEvent event);

    void updateCarPrice(CarPriceUpdateEvent event);

    void addBooking(BookingCreateEvent event);

    void cancelBooking(BookingCancelEvent event);

    void completeBooking(BookingCompletionEvent event);
}
