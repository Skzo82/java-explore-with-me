package ru.practicum.stats.client;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.util.List;

public interface StatsClient {

    /* # Сохранение хита (запроса) */
    void saveHit(HttpServletRequest request, String app);

    /* # Получение статистики просмотров */
    List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique);
}
