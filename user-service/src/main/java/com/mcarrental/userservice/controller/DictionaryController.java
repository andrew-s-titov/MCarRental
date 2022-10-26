package com.mcarrental.userservice.controller;

import com.mcarrental.userservice.enums.EnumWithPrettyName;
import com.mcarrental.userservice.enums.LocalizedEnum;
import com.mcarrental.userservice.enums.CompanyType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/dictionary/user")
public class DictionaryController {

    @GetMapping("/company_type")
    public ResponseEntity<Map<?, String>> legalEntityForm() {
        return ResponseEntity.ok(enumToMap(CompanyType.class));
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