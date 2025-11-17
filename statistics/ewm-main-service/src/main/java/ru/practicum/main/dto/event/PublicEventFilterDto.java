package ru.practicum.main.dto.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/* # Параметры публичного поиска событий (query-параметры /events) */
@Getter
@Setter
public class PublicEventFilterDto {

    /* # Текст для поиска в аннотации/описании (опционально) */
    private String text;

    /* # Список id категорий (опционально) */
    private List<Long> categories;

    /* # Платные/бесплатные (опционально) */
    private Boolean paid;

    /* # Начало диапазона дат (опционально) */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    /* # Конец диапазона дат (опционально) */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    /* # Только события, где ещё есть свободные места (опционально) */
    private Boolean onlyAvailable;

    /* # Поле сортировки: EVENT_DATE / VIEWS (опционально) */
    private String sort;
}
