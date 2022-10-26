package com.mcarrental.carservice.dto.car;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.carservice.dto.BaseBookingDateRequest;
import com.mcarrental.carservice.enums.Brand;
import com.mcarrental.carservice.enums.CarType;
import com.mcarrental.carservice.enums.Fuel;
import com.mcarrental.carservice.enums.GearBox;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarSearchRequestDTO extends BaseBookingDateRequest {

    private List<CarType> types;

    private List<Fuel> fuelTypes;

    private List<GearBox> gearBoxTypes;

    private List<Brand> brands;

    @Override
    public String toString() {
        return "CarSearchRequestDTO(" + super.toString() +
                ", types: " + types +
                ", fuelTypes: " + fuelTypes +
                ", gearBoxTypes: " + gearBoxTypes +
                ", brands: " + brands + ")";
    }
}