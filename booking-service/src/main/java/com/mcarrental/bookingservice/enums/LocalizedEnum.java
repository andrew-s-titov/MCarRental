package com.mcarrental.bookingservice.enums;

import com.mcarrental.bookingservice.config.LocaleSupport;

public interface LocalizedEnum {

    String getBundleResourceKey();

    default String getLocalizedName() {
        return LocaleSupport.getBundle().getString(getBundleResourceKey());
    }
}