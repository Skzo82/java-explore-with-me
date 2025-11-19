package ru.practicum.main.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/* # Конфигурация RestTemplate для обращения к stats-service */
@Configuration
public class RestTemplateConfig {

    /* # Бин RestTemplate, который будет внедряться в StatsClientRest */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}