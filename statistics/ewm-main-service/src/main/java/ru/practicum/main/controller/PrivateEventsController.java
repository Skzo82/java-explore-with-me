package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.event.*;
import ru.practicum.main.service.EventService;

import java.util.List;

/* # Приватные эндпоинты владельца событий */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {

    private final EventService service;

    @PostMapping
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody NewEventDto dto) {
        /* # Создание события пользователем */
        return service.create(userId, dto);
    }

    @GetMapping
    public List<EventShortDto> myEvents(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        /* # Преобразуем from/size в Pageable */
        Pageable pageable = PageRequest.of(from / size, size);
        return service.getUserEvents(userId, pageable);
    }

    @GetMapping("/{eventId}")
    public EventFullDto myEvent(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        /* # Получить своё событие по id */
        return service.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody UpdateEventUserRequest dto) {
        /* # Обновление события владельцем */
        return service.updateByUser(userId, eventId, dto);
    }

    @PatchMapping("/{eventId}/cancel")
    public EventFullDto cancel(@PathVariable Long userId,
                               @PathVariable Long eventId) {
        /* # Отмена события владельцем */
        return service.cancelByUser(userId, eventId);
    }
}