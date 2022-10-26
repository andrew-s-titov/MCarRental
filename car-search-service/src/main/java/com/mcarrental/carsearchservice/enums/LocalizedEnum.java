package com.mcarrental.carsearchservice.enums;

import com.mcarrental.carsearchservice.config.LocaleSupport;

public interface LocalizedEnum {

    String getBundleResourceKey();

    default String getLocalizedName() {
        return LocaleSupport.getBundle().getString(getBundleResourceKey());
    }
}