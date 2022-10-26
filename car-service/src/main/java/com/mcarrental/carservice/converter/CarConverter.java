package com.mcarrental.carservice.converter;

import com.mcarrental.carservice.dto.car.CarCreateRequestDTO;
import com.mcarrental.carservice.dto.car.CarViewDTO;
import com.mcarrental.carservice.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarConverter {

    public Car fromCreateRequest(CarCreateRequestDTO createRequest) {
        return Car.builder()
                .type(createRequest.getType())
                .brand(createRequest.getBrand())
                .model(createRequest.getModel())
                .gearBox(createRequest.getGearBox())
                .engineCapacity(createRequest.getEngineCapacity())
                .fuel(createRequest.getFuel())
                .layout(createRequest.getLayout())
                .pricePerDay(createRequest.getPricePerDay())
                .productionYear(createRequest.getProductionYear().getValue())
                .numberOfSeats(createRequest.getNumberOfSeats())
                .numberPlate(createRequest.getNumberPlate())
                .vin((createRequest.getVin()))
                .build();
    }

    public CarViewDTO toView(Car car) {
        return CarViewDTO.builder()
                .id(car.getId())
                .type(car.getType())
                .brand(car.getBrand())
                .model(car.getModel())
                .gearBox(car.getGearBox())
                .engineCapacity(car.getEngineCapacity())
                .fuel(car.getFuel())
                .layout(car.getLayout())
                .pricePerDay(car.getPricePerDay())
                .productionYear(car.getProductionYear())
                .numberOfSeats(car.getNumberOfSeats())
                .numberPlate(car.getNumberPlate())
                .vin((car.getVin()))
                .ownerId(car.getOwnerId())
                .visible(car.isVisible())
                .build();
    }
}