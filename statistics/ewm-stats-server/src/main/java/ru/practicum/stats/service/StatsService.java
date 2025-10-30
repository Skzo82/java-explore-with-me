package ru.practicum.stats.service;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

/* Сервисный интерфейс статистики */
public interface StatsService {
    void save(EndpointHitDto dto);

    List<ViewStatsDto> stats(LocalDateTime start,
                             LocalDateTime end,
                             List<String> uris,
                             boolean unique);
}