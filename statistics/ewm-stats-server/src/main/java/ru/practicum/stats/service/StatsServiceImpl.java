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

    @Autowired
    public StatsServiceImpl(HitRepository repo) {
        this.repo = repo;
    }

    // Сохранение хита (DTO → сущность)
    @Override
    public void save(EndpointHitDto dto) {
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

    // Получение статистики по параметрам
    @Override
    public List<ViewStatsDto> stats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    boolean unique) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start must be before end");
        }

        // если список URI передан
        if (uris != null && !uris.isEmpty()) {
            return unique
                    ? repo.aggregateUniqueByUris(start, end, uris)
                    : repo.aggregateAllByUris(start, end, uris);
        }

        // без фильтра по URI
        return unique
                ? repo.aggregateUnique(start, end)
                : repo.aggregateAll(start, end);
    }
}