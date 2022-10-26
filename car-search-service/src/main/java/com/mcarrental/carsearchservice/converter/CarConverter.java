package com.mcarrental.carsearchservice.converter;

import com.mcarrental.carsearchservice.dto.CarViewDTO;
import com.mcarrental.carsearchservice.event.CarCreateEvent;
import com.mcarrental.carsearchservice.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarConverter {

    public Car fromCreateEvent(CarCreateEvent event) {
        return Car.builder()
                .carId(event.getCarId())
                .type(event.getType())
                .brand(event.getBrand())
                .model(event.getModel())
                .fuel(event.getFuel())
                .gearBox(event.getGearBox())
                .engineCapacity(event.getEngineCapacity())
                .layout(event.getLayout())
                .productionYear(event.getProductionYear())
                .numberOfSeats(event.getNumberOfSeats())
                .pricePerDay(event.getPricePerDay())
                .build();
    }

    public CarViewDTO toView(Car car) {
        return CarViewDTO.builder()
                .id((car.getCarId()))
                .brand(car.getBrand())
                .model(car.getModel())
                .productionYear(car.getProductionYear())
                .fuel(car.getFuel())
                .gearBox(car.getGearBox())
                .engineCapacity(car.getEngineCapacity())
                .layout(car.getLayout())
                .numberOfSeats(car.getNumberOfSeats())
                .type(car.getType())
                .pricePerDay(car.getPricePerDay())
                .build();
    }
}
