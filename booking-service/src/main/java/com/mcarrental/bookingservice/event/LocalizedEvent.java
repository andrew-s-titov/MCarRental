package com.mcarrental.bookingservice.event;

import java.util.Locale;

public interface LocalizedEvent {

    Locale getLocale();

    void setLocale(Locale locale);
}