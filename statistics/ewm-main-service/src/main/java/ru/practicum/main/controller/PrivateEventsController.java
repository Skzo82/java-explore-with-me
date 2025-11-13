package ru.practicum.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.NewEventDto;
import ru.practicum.main.dto.event.UpdateEventUserRequest;
import ru.practicum.main.service.EventService;

import java.util.List;

/* # Приватные эндпоинты владельца событий */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventsController {

    private final EventService service;

    /* # Создание события пользователем -> 201 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @Valid @RequestBody NewEventDto dto) {
        return service.create(userId, dto);
    }

    @GetMapping
    public List<EventShortDto> myEvents(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        /* # Преобразуем from/size в Pageable */
        Pageable pageable = PageRequest.of(from / size, size);
        return service.getUserEvents(userId, pageable);
    }

    @GetMapping("/{eventId}")
    public EventFullDto myEvent(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return service.getUserEvent(userId, eventId);
    }

    /* # Обновление события владельцем: включаем валидацию тела -> 400 при нарушениях */
    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest dto) {
        return service.updateByUser(userId, eventId, dto);
    }

    @PatchMapping("/{eventId}/cancel")
    public EventFullDto cancel(@PathVariable Long userId,
                               @PathVariable Long eventId) {
        return service.cancelByUser(userId, eventId);
    }
}