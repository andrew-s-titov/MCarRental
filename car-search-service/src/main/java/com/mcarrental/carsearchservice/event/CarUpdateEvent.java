package com.mcarrental.carsearchservice.event;

import com.mcarrental.carsearchservice.enums.Brand;
import com.mcarrental.carsearchservice.enums.CarType;
import com.mcarrental.carsearchservice.enums.Fuel;
import com.mcarrental.carsearchservice.enums.GearBox;
import com.mcarrental.carsearchservice.enums.VehicleLayout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarUpdateEvent implements CarEvent {

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
}