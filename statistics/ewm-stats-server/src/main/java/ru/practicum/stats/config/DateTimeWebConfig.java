package ru.practicum.stats.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* Конфигурация MVC: регистрируем форматтер для LocalDateTime */
@Configuration
public class DateTimeWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new MultiLocalDateTimeFormatter());
    }
}
