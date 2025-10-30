package ru.practicum.stats.client;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.util.List;

public interface StatsClient {
    // отправляет запись хита в сервис статистики
    void saveHit(EndpointHitDto dto);

    // запрашивает статистику за период
    List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique);
}