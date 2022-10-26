package com.mcarrental.userservice.enums;

import com.mcarrental.userservice.config.LocaleSupport;

public interface LocalizedEnum {

    String getBundleResourceKey();

    default String getLocalizedName() {
        return LocaleSupport.getBundle().getString(getBundleResourceKey());
    }
}