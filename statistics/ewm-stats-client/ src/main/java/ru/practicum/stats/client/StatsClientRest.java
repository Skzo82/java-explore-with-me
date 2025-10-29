package ru.practicum.stats.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatsClientRest implements StatsClient {

    // формат дат запроса в UTC
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("UTC"));

    private final RestTemplate rt;
    private final String baseUrl;

    public StatsClientRest(RestTemplate rt, String baseUrl) {
        this.rt = rt;
        this.baseUrl = baseUrl;
    }

    @Override
    public void saveHit(EndpointHitDto dto) {
        // отправка POST /hit
        rt.postForEntity(baseUrl + "/hit", dto, Void.class);
    }

    @Override
    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique) {
        StringBuilder url = new StringBuilder(baseUrl + "/stats?start={start}&end={end}&unique={unique}");
        if (uris != null && !uris.isEmpty()) {
            for (String u : uris) {
                url.append("&uris=").append(u);
            }
        }
        ResponseEntity<ViewStatsDto[]> resp = rt.getForEntity(
                url.toString(),
                ViewStatsDto[].class,
                Map.of("start", F.format(start), "end", F.format(end), "unique", unique)
        );
        ViewStatsDto[] body = resp.getBody();
        return body == null ? List.of() : List.of(body);
    }
}