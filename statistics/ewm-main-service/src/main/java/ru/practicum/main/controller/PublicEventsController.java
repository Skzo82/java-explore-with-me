package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> searchPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false, name = "categories") List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, name = "rangeStart")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
            @RequestParam(required = false, name = "rangeEnd")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            Pageable pageable) {
        return eventService.getPublicEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, pageable);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id) {
        return eventService.getPublishedEventById(id);
    }
}