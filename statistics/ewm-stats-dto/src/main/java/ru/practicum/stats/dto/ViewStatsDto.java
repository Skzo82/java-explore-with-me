package ru.practicum.stats.dto;

/* # DTO для агрегированной статистики просмотров */
public record ViewStatsDto(
        String app,   // название сервиса
        String uri,   // URI ресурса
        long hits     // количество просмотров
) {
}