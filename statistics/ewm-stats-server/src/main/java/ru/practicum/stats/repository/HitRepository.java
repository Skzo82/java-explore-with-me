package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    // агрегирует все хиты
    @Query("""
                SELECT new ru.practicum.stats.dto.ViewStatsDto(h.app, h.uri, COUNT(h.ip))
                FROM Hit h
                WHERE h.timestamp BETWEEN :start AND :end
                  AND (:uris IS NULL OR h.uri IN :uris)
                GROUP BY h.app, h.uri
                ORDER BY COUNT(h.ip) DESC
            """)
    List<ViewStatsDto> aggregateAll(LocalDateTime start, LocalDateTime end, List<String> uris);

    // агрегирует уникальные хиты (по IP)
    @Query("""
                SELECT new ru.practicum.stats.dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip))
                FROM Hit h
                WHERE h.timestamp BETWEEN :start AND :end
                  AND (:uris IS NULL OR h.uri IN :uris)
                GROUP BY h.app, h.uri
                ORDER BY COUNT(DISTINCT h.ip) DESC
            """)
    List<ViewStatsDto> aggregateUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}