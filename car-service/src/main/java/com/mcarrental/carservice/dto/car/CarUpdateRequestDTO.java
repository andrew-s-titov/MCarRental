package com.mcarrental.carservice.dto.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.Year;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarUpdateRequestDTO {

    private CarType type;

    private Fuel fuel;

    private GearBox gearBox;

    private Brand brand;

    private String model;

    @PastOrPresent(message = "Production year cannot be later than current year")
    private Year productionYear;

    @DecimalMin("1.0")
    @DecimalMax("6.0")
    private Double engineCapacity;

    @Positive
    private Integer numberOfSeats;

    private VehicleLayout layout;

    private String numberPlate;

    private String vin;

    @JsonIgnore
    @Min(value = 2000, message = "Production year cannot be earlier than 2000")
    private int productionYear() {
        return productionYear.getValue();
    }
}