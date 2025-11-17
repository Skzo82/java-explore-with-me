package ru.practicum.main.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/* # Параметры фильтрации событий для админ-эндпоинта */
@Getter
@Setter
public class AdminEventFilterDto {
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}