package com.mcarrental;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.crypto.RsaKeyConversionServicePostProcessor;

@Configuration
public class TestConfig {

    @Bean
    public static BeanFactoryPostProcessor conversionServicePostProcessor() {
        return new RsaKeyConversionServicePostProcessor();
    }
}