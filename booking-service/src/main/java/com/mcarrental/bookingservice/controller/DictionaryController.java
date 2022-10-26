package com.mcarrental.bookingservice.controller;

import com.mcarrental.bookingservice.enums.BookTime;
import com.mcarrental.bookingservice.enums.BookingCompletion;
import com.mcarrental.bookingservice.enums.BookingStatusFilter;
import com.mcarrental.bookingservice.enums.EnumWithPrettyName;
import com.mcarrental.bookingservice.enums.LocalizedEnum;
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

    @GetMapping("/booking/book_time")
    public ResponseEntity<Map<?, String>> book_time() {
        return ResponseEntity.ok(enumToMap(BookTime.class));
    }

    @GetMapping("/booking/status")
    public ResponseEntity<Map<?, String>> status() {
        return ResponseEntity.ok(enumToMap(BookingStatusFilter.class));
    }

    @GetMapping("/booking/completion_status")
    public ResponseEntity<Map<?, String>> completionStatus() {
        return ResponseEntity.ok(enumToMap(BookingCompletion.class));
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