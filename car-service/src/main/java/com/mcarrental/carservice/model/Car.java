package com.mcarrental.carservice.model;

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
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(schema = "public", name = "cars")
public class Car {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "visible", nullable = false)
    private boolean visible;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CarType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "fuel", nullable = false)
    private Fuel fuel;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gear_box", nullable = false)
    private GearBox gearBox;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "brand", nullable = false)
    private Brand brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "production_year", nullable = false)
    private Integer productionYear;

    @Column(name = "engine_capacity", nullable = false)
    private Double engineCapacity;

    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "layout", nullable = false)
    private VehicleLayout layout;

    @Column(name = "price_per_day", nullable = false)
    private Integer pricePerDay;

    @Column(name = "number_plate", nullable = false)
    private String numberPlate;

    @Column(name = "vin", nullable = false)
    private String vin;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    public String getShortInfo() {
        return brand.getPrettyName() + " " + model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id)
                && brand == car.brand && Objects.equals(model, car.model)
                && numberPlate.equals(car.numberPlate)
                && vin.equals(car.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, numberPlate, vin);
    }
}