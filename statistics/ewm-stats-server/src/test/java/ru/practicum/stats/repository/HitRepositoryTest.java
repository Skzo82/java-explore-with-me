package ru.practicum.stats.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HitRepositoryTest {

    @Autowired
    HitRepository repo;

    @Test
    void aggregateAll_andUnique_countsOk() {
        // подготавливаем данные
        repo.save(hit("app", "/a", "1.1.1.1", "2025-10-27T12:00:00"));
        repo.save(hit("app", "/a", "1.1.1.1", "2025-10-27T12:05:00"));
        repo.save(hit("app", "/a", "2.2.2.2", "2025-10-27T12:10:00"));
        repo.save(hit("app", "/b", "9.9.9.9", "2025-10-27T12:20:00"));

        var s = LocalDateTime.parse("2025-10-27T00:00:00");
        var e = LocalDateTime.parse("2025-10-27T23:59:59");

        List<ViewStatsDto> all = repo.aggregateAll(s, e, null);
        List<ViewStatsDto> unique = repo.aggregateUnique(s, e, null);

        // проверяем суммы по /a
        var allForA = all.stream().filter(v -> "/a".equals(v.uri())).findFirst().orElseThrow();
        var uniqForA = unique.stream().filter(v -> "/a".equals(v.uri())).findFirst().orElseThrow();

        assertThat(allForA.hits()).isEqualTo(3L);
        assertThat(uniqForA.hits()).isEqualTo(2L);
    }

    private static Hit hit(String app, String uri, String ip, String iso) {
        var h = new Hit();
        h.setApp(app);
        h.setUri(uri);
        h.setIp(ip);
        h.setTimestamp(LocalDateTime.parse(iso));
        return h;
    }
}