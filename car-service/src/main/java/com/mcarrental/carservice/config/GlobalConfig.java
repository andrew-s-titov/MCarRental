package com.mcarrental.carservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableAspectJAutoProxy
public class GlobalConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder jacksonBootDefault) {
        return jacksonBootDefault.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(LocaleSupport.DEFAULT_LOCALE);
        resolver.setSupportedLocales(LocaleSupport.SUPPORTED_LOCALES);
        return resolver;
    }
}
