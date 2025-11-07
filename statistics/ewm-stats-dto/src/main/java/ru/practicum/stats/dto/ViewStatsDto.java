package ru.practicum.stats.dto;

// DTO ответа — соответствует схеме "ViewStats"
public record ViewStatsDto(String app, String uri, long hits) {
}