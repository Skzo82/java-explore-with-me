package ru.practicum.stats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/* Реализация сервиса статистики */
@Service
public class StatsServiceImpl implements StatsService {

    private final HitRepository repo;

    // 👇 Добавляем явный конструктор для Spring
    @Autowired
    public StatsServiceImpl(HitRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(EndpointHitDto dto) {
        // маппинг DTO -> сущность
        Hit h = new Hit();
        h.setApp(dto.app());
        h.setUri(dto.uri());
        h.setIp(dto.ip());

        // парсим timestamp, поддерживая оба формата
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        };

        LocalDateTime parsed = null;
        for (DateTimeFormatter fmt : formats) {
            try {
                parsed = LocalDateTime.parse(dto.timestamp(), fmt);
                break;
            } catch (Exception ignored) {
            }
        }

        if (parsed == null) {
            throw new IllegalArgumentException("Invalid timestamp format: " + dto.timestamp());
        }

        h.setTimestamp(parsed);
        repo.save(h);
    }

    @Override
    public List<ViewStatsDto> stats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    boolean unique) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start must be before end");
        }
        return unique
                ? repo.aggregateUnique(start, end, emptyToNull(uris))
                : repo.aggregateAll(start, end, emptyToNull(uris));
    }

    private static List<String> emptyToNull(List<String> uris) {
        return (uris == null || uris.isEmpty()) ? null : uris;
    }
}