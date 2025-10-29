package ru.practicum.stats.dto;

// DTO запроса — соответствует схеме "EndpointHit"
public record EndpointHitDto(Long id, String app, String uri, String ip, String timestamp) {}