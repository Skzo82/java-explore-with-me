package ru.practicum.main.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

/* # Публичные эндпоинты событий */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class PublicEventsController {

    private final EventService eventService;

    /* # Публичный поиск событий (с поддержкой from/size и формата дат из ТЗ) */
    @GetMapping("/events")
    public List<EventShortDto> searchPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false, name = "categories") List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, name = "rangeStart")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false, name = "rangeEnd")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        /* # Преобразуем from/size в Pageable */
        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, pageable);
    }

    /* # Публичное получение опубликованного события по id */
    @GetMapping("/events/{eventId}")
    public EventFullDto getPublicById(@PathVariable Long eventId) {
        return eventService.getPublishedEventById(eventId);
    }
}