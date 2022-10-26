package com.mcarrental.carservice.enums;

import com.mcarrental.carservice.config.LocaleSupport;

public interface LocalizedEnum {

    String getBundleResourceKey();

    default String getLocalizedName() {
        return LocaleSupport.getBundle().getString(getBundleResourceKey());
    }
}