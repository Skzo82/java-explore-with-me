package ru.practicum.stats.config;

import org.springframework.format.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* Парсер для LocalDateTime, который принимает несколько форматов даты */
public class MultiLocalDateTimeFormatter implements Formatter<LocalDateTime> {

    // поддерживаемые форматы: с пробелом и с 'T'
    private static final List<DateTimeFormatter> SUPPORTED = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    );

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        // пробуем последовательно каждый формат
        for (DateTimeFormatter f : SUPPORTED) {
            try {
                return LocalDateTime.parse(text, f);
            } catch (DateTimeParseException ignore) {
                // пробуем следующий
            }
        }
        // если ничего не подошло — кидаем понятную ошибку
        throw new IllegalArgumentException("Invalid date-time format: " + text +
                " (supported: yyyy-MM-dd HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss)");
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        // печатаем в формате спецификации (с пробелом)
        return object.format(SUPPORTED.get(0));
    }
}
