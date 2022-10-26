package com.mcarrental.carservice.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcarrental.carservice.model.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CarPriceUpdateEvent implements CarEvent {

    private UUID carId;

    private Integer pricePerDay;

    @JsonIgnore
    public static CarPriceUpdateEvent fromCar(Car car) {
        return new CarPriceUpdateEvent(car.getId(), car.getPricePerDay());
    }
}