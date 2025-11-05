package ru.practicum.stats.client;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.util.List;

public interface StatsClient {
    void saveHit(HttpServletRequest request, String app);

    List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique);
}