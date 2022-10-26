package com.mcarrental.carservice.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcarrental.carservice.enums.Brand;
import com.mcarrental.carservice.enums.CarType;
import com.mcarrental.carservice.enums.Fuel;
import com.mcarrental.carservice.enums.GearBox;
import com.mcarrental.carservice.enums.VehicleLayout;
import com.mcarrental.carservice.model.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CarCreateEvent implements CarEvent {

    private UUID carId;


    private CarType type;

    private Fuel fuel;

    private GearBox gearBox;

    private Brand brand;

    private String model;

    private Year productionYear;

    private Double engineCapacity;

    private Integer numberOfSeats;

    private VehicleLayout layout;

    private Integer pricePerDay;

    @JsonIgnore
    public static CarCreateEvent fromCar(Car car) {
        return new CarCreateEvent(car.getId(), car.getType(), car.getFuel(), car.getGearBox(),
                car.getBrand(), car.getModel(), Year.of(car.getProductionYear()),
                car.getEngineCapacity(), car.getNumberOfSeats(), car.getLayout(), car.getPricePerDay());
    }
}