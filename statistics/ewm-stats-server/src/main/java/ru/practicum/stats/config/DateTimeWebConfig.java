package ru.practicum.stats.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/* Конфигурация форматирования даты-времени для параметров запросов */
@Configuration
public class DateTimeWebConfig implements WebMvcConfigurer {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) {
                return LocalDateTime.parse(text, FORMATTER);
            }

            @Override
            public String print(LocalDateTime object, Locale locale) {
                return FORMATTER.format(object);
            }
        });
    }
}