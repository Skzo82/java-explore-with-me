package ru.practicum.stats.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

/* REST-контроллер сервиса статистики */
@RestController
public class StatsController {

    private final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHitDto dto) {
        service.save(dto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> stats(
            @RequestParam("start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,

            @RequestParam("end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime end,

            @RequestParam(value = "uris", required = false)
            List<String> uris,

            @RequestParam(value = "unique", defaultValue = "false")
            boolean unique
    ) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start must be before end");
        }
        return service.stats(start, end, uris, unique);
    }
}