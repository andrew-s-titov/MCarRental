package com.mcarrental.carservice.controller;

import com.mcarrental.carservice.enums.Brand;
import com.mcarrental.carservice.enums.CarType;
import com.mcarrental.carservice.enums.EnumWithPrettyName;
import com.mcarrental.carservice.enums.Fuel;
import com.mcarrental.carservice.enums.GearBox;
import com.mcarrental.carservice.enums.LocalizedEnum;
import com.mcarrental.carservice.enums.VehicleLayout;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @GetMapping("/car/brand")
    public ResponseEntity<Map<?, String>> brand() {
        return ResponseEntity.ok(enumToMap(Brand.class));
    }

    @GetMapping("/car/type")
    public ResponseEntity<Map<?, String>> type() {
        return ResponseEntity.ok(enumToMap(CarType.class));
    }

    @GetMapping("/car/fuel")
    public ResponseEntity<Map<?, String>> fuel() {
        return ResponseEntity.ok(enumToMap(Fuel.class));
    }

    @GetMapping("/car/gear_box")
    public ResponseEntity<Map<?, String>> gearBox() {
        return ResponseEntity.ok(enumToMap(GearBox.class));
    }

    @GetMapping("/car/layout")
    public ResponseEntity<Map<?, String>> layout() {
        return ResponseEntity.ok(enumToMap(VehicleLayout.class));
    }

    private <T extends Enum<T>> EnumMap<T, String> enumToMap(Class<T> tEnum) {
        EnumMap<T, String> enumMap = new EnumMap<>(tEnum);
        for (T enumConstant : tEnum.getEnumConstants()) {
            enumMap.put(enumConstant, retrieveEnumValue(tEnum).apply(enumConstant));
        }
        return enumMap;
    }

    private <T extends Enum<T>> Function<T, String> retrieveEnumValue(Class<T> tEnum) {
        if (EnumWithPrettyName.class.isAssignableFrom(tEnum)) {
            return enumeration -> ((EnumWithPrettyName) enumeration).getPrettyName();
        }
        if (LocalizedEnum.class.isAssignableFrom(tEnum)) {
            return enumeration -> ((LocalizedEnum) enumeration).getLocalizedName();
        }
        return Enum::name;
    }
}