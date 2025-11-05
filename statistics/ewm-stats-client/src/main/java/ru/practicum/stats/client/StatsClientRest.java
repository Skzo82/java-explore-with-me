package ru.practicum.stats.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class StatsClientRest implements StatsClient {

    private final RestTemplate rt;

    @Value("${stats.base-url:http://localhost:9090}")
    private String baseUrl;

    private static final DateTimeFormatter F =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public StatsClientRest(RestTemplateBuilder rtb) {
        this.rt = rtb
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void saveHit(HttpServletRequest request, String app) {
        String uri = request.getRequestURI();
        String ip = clientIp(request);
        String ts = F.format(Instant.now());

        EndpointHitDto dto = new EndpointHitDto(
                null,          // id
                app,           // app
                uri,           // uri
                ip,            // ip
                ts             // timestamp "yyyy-MM-dd HH:mm:ss"
        );

        rt.postForLocation(baseUrl + "/hit", dto);
    }

    @Override
    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique) {
        String qs = "start=" + enc(F.format(start)) +
                "&end=" + enc(F.format(end)) +
                "&unique=" + unique;

        if (uris != null && !uris.isEmpty()) {
            // ВАЖНО: список через запятую, затем целиком URL-encode
            String joined = String.join(",", uris);
            qs += "&uris=" + enc(joined);
        }

        ResponseEntity<ViewStatsDto[]> resp =
                rt.getForEntity(baseUrl + "/stats?" + qs, ViewStatsDto[].class);

        ViewStatsDto[] body = resp.getBody();
        return Arrays.asList(body == null ? new ViewStatsDto[0] : body);
    }

    private static String clientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}