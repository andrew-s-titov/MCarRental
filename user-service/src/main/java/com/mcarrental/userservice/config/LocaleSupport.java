package com.mcarrental.userservice.config;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocaleSupport {

    private final static String BUNDLE_BASENAME = "i18n/messages";
    private final static Map<Locale, ResourceBundle> BUNDLES = new HashMap<>();

    public final static Locale DEFAULT_LOCALE = new Locale("ru");
    public final static List<Locale> SUPPORTED_LOCALES = List.of(Locale.ENGLISH, Locale.US, DEFAULT_LOCALE);

    static {
        SUPPORTED_LOCALES.forEach(locale -> BUNDLES.put(locale, ResourceBundle.getBundle(BUNDLE_BASENAME, locale)));
    }

    public static ResourceBundle getBundle() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (SUPPORTED_LOCALES.contains(currentLocale)) {
            return BUNDLES.get(currentLocale);
        }
        return BUNDLES.get(DEFAULT_LOCALE);
    }
}