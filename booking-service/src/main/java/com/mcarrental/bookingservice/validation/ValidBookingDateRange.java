package com.mcarrental.bookingservice.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can validate implementations of BookingDateRange
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BookingDateRangeValidator.class})
public @interface ValidBookingDateRange {

    int minBookingHours() default 0;

    int minHoursTillBooking() default 0;

    String message() default "Not valid booking date range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
