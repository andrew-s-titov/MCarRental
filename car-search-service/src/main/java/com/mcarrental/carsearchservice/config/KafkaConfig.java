package com.mcarrental.carsearchservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;

@Configuration
public class KafkaConfig {

    @Bean
    public BytesJsonMessageConverter bytesJsonMessageConverter() {
        return new BytesJsonMessageConverter();
    }
}
