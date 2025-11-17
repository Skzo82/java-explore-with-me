package ru.practicum.main.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/* # Параметры фильтрации публичного поиска событий */
@Getter
@Setter
public class PublicEventFilterDto {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort; // EVENT_DATE / VIEWS / null
}