package ru.practicum.stats.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    private final HitRepository repo;

    public StatsServiceImpl(HitRepository repo) {
        this.repo = repo;
    }

    // # Поддерживаем оба формата времени: с пробелом и с 'T'
    private static final DateTimeFormatter[] TS_FORMATS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    };

    @Override
    @Transactional
    public void save(EndpointHitDto dto) {
        Hit h = new Hit();
        h.setApp(dto.app());
        h.setUri(dto.uri());
        h.setIp(dto.ip());
        // # timestamp из DTO — строка, парсим в LocalDateTime
        h.setTimestamp(parseTimestamp(dto.timestamp()));
        repo.save(h);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> stats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    boolean unique) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start must be before end");
        }
        boolean hasUris = uris != null && !uris.isEmpty();

        if (unique) {
            return hasUris
                    ? repo.aggregateUniqueByUris(start, end, uris)
                    : repo.aggregateUnique(start, end);
        } else {
            return hasUris
                    ? repo.aggregateAllByUris(start, end, uris)
                    : repo.aggregateAll(start, end);
        }
    }

    /* # Парсим строковый timestamp в LocalDateTime, пробуя несколько форматов */
    private static LocalDateTime parseTimestamp(String ts) {
        for (DateTimeFormatter f : TS_FORMATS) {
            try {
                return LocalDateTime.parse(ts, f);
            } catch (Exception ignored) {
            }
        }
        throw new IllegalArgumentException("Invalid timestamp format: " + ts);
    }
}