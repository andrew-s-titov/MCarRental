package com.mcarrental.carsearchservice.service.impl;

import com.mcarrental.carsearchservice.converter.BookingConverter;
import com.mcarrental.carsearchservice.converter.CarConverter;
import com.mcarrental.carsearchservice.event.BookingCancelEvent;
import com.mcarrental.carsearchservice.event.BookingCompletionEvent;
import com.mcarrental.carsearchservice.event.BookingCreateEvent;
import com.mcarrental.carsearchservice.event.CarCreateEvent;
import com.mcarrental.carsearchservice.event.CarPriceUpdateEvent;
import com.mcarrental.carsearchservice.event.CarUpdateEvent;
import com.mcarrental.carsearchservice.model.Car;
import com.mcarrental.carsearchservice.repository.elastic.CarRepository;
import com.mcarrental.carsearchservice.service.CarSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ElasticCarSyncServiceImpl implements CarSyncService {

    private final CarRepository carRepository;
    private final BookingConverter bookingConverter;
    private final CarConverter converter;

    @KafkaListener(topics = "${kafka.topic.car.create}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void createNewCar(CarCreateEvent event) {
        log.info("Create event received from Kafka for car " + event.getCarId());
        var car = converter.fromCreateEvent(event);
        carRepository.save(car);
        log.info("Car with id {} saved to ES", car.getCarId());
    }

    @KafkaListener(topics = "${kafka.topic.car.update}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void updateCar(CarUpdateEvent event) {
        log.info("Update event received from Kafka for car " + event.getCarId());
        var carId = event.getCarId();
        var car = safeGetCar(carId);
        boolean hasNewData = mergeUpdateWithCar(car, event);
        if (hasNewData) {
            carRepository.save(car);
            log.info("Car with id {} updated in ES", car.getCarId());
        }
    }

    @KafkaListener(topics = "${kafka.topic.car.update-price}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void updateCarPrice(CarPriceUpdateEvent event) {
        log.info("Price update event received from Kafka for car " + event.getCarId());
        var carId = event.getCarId();
        var car = safeGetCar(carId);
        var newPrice = event.getPricePerDay();
        if (newPrice != null && !newPrice.equals(car.getPricePerDay())) {
            car.setPricePerDay(newPrice);
            carRepository.save(car);
            log.info("Updated price per day for car with id {} in ES", car.getCarId());
        }
    }

    @KafkaListener(topics = "${kafka.topic.booking.create}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void addBooking(BookingCreateEvent event) {
        var bookingId = event.getBookingId();
        var carId = event.getCarId();
        log.info("Create event received from Kafka for booking {} for car {}", bookingId, carId);
        var car = safeGetCar(carId);
        var newBooking = bookingConverter.fromBookingCreateEvent(event);
        if (car.addBooking(newBooking)) {
            carRepository.save(car);
            log.info("Booking {} saved to ES for car {}", bookingId, carId);
        }
    }

    @KafkaListener(topics = {"${kafka.topic.booking.decline}", "${kafka.topic.booking.abort}"}, groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void cancelBooking(BookingCancelEvent event) {
        var bookingId = event.getBookingId();
        var carId = event.getCarId();
        log.info("Cancel event received from Kafka for booking {} for car {}", bookingId, carId);
        removeBookingEntry(carId, bookingId);
    }

    @KafkaListener(topics = {"${kafka.topic.booking.complete.normal}", "${kafka.topic.booking.complete.delayed}",
            "${kafka.topic.booking.complete.damaged}"}, groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void completeBooking(BookingCompletionEvent event) {
        var carId = event.getCarId();
        var bookingId = event.getBookingId();
        log.info("Booking completion event received from Kafka for booking {} for car {}", bookingId, carId);
        removeBookingEntry(carId, bookingId);
    }

    private Car safeGetCar(UUID carId) {
        return carRepository.findById(carId).orElseThrow(() ->
                new IllegalStateException(String.format("Car with id %s not found", carId)));
    }

    private boolean mergeUpdateWithCar(Car car, CarUpdateEvent event) {
        boolean hasNewData = false;

        var newCarType = event.getType();
        if (newCarType != null && !newCarType.equals(car.getType())) {
            car.setType(newCarType);
            hasNewData = true;
        }

        var newBrand = event.getBrand();
        if (newBrand != null && !newBrand.equals(car.getBrand())) {
            car.setBrand(newBrand);
            hasNewData = true;
        }

        var newModel = event.getModel();
        if (StringUtils.isNotBlank(newModel) && !newModel.equals(car.getModel())) {
            car.setModel(newModel);
            hasNewData = true;
        }

        var newFuel = event.getFuel();
        if (newFuel != null && !newFuel.equals(car.getFuel())) {
            car.setFuel(newFuel);
            hasNewData = true;
        }

        var newVehicleLayout = event.getLayout();
        if (newVehicleLayout != null && !newVehicleLayout.equals(car.getLayout())) {
            car.setLayout(newVehicleLayout);
            hasNewData = true;
        }

        var newGearBox = event.getGearBox();
        if (newGearBox != null && !newGearBox.equals(car.getGearBox())) {
            car.setGearBox(newGearBox);
            hasNewData = true;
        }

        var newEngineCapacity = event.getEngineCapacity();
        if (newEngineCapacity != null && !newEngineCapacity.equals(car.getEngineCapacity())) {
            car.setEngineCapacity(newEngineCapacity);
            hasNewData = true;
        }

        var newProdYear = event.getProductionYear();
        if (newProdYear != null && !newProdYear.equals(car.getProductionYear())) {
            car.setProductionYear(newProdYear);
            hasNewData = true;
        }

        var newNumberOfSeats = event.getNumberOfSeats();
        if (newNumberOfSeats != null && !newNumberOfSeats.equals(car.getNumberOfSeats())) {
            car.setNumberOfSeats(newNumberOfSeats);
            hasNewData = true;
        }

        return hasNewData;
    }

    private void removeBookingEntry(UUID carId, UUID bookingId) {
        var car = safeGetCar(carId);
        car.getBookings().stream()
                .filter(booking -> bookingId.equals(booking.getId()))
                .findAny()
                .ifPresent(booking -> {
                    if (car.removeBooking(booking)) {
                        carRepository.save(car);
                        log.info("Booking {} removed from ES for car {}", bookingId, carId);
                    }
                });
    }
}