package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.UpdateEventAdminRequest;
import ru.practicum.main.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

/* # Админ-эндпоинты событий */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventsController {

    private final EventService service;

    @GetMapping
    public List<EventFullDto> search(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        /* # Преобразуем from/size в Pageable */
        Pageable pageable = PageRequest.of(from / size, size);
        return service.searchAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByAdmin(@PathVariable Long eventId,
                                      @RequestBody UpdateEventAdminRequest dto) {
        /* # Обновление события администратором */
        return service.updateByAdmin(eventId, dto);
    }
}