package com.mcarrental.carservice.dto.car;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.carservice.enums.Brand;
import com.mcarrental.carservice.enums.CarType;
import com.mcarrental.carservice.enums.Fuel;
import com.mcarrental.carservice.enums.GearBox;
import com.mcarrental.carservice.enums.VehicleLayout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarViewDTO {

    private UUID id;

    private CarType type;

    private Fuel fuel;

    private GearBox gearBox;

    private Brand brand;

    private String model;

    private Integer productionYear;

    private Double engineCapacity;

    private Integer numberOfSeats;

    private VehicleLayout layout;

    private Integer pricePerDay;

    private String numberPlate;

    private String vin;

    private boolean visible;

    private UUID ownerId;
}
