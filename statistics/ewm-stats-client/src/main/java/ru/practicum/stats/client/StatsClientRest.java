package ru.practicum.stats.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class StatsClientRest implements StatsClient {

    private final RestTemplate rt;

    @Value("${app.stats.base-url:http://localhost:9090}")
    private String baseUrl;

    // Используем UTC, чтобы избежать рассинхронизации между средами
    private static final DateTimeFormatter F =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

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
                null,   // id заполняется на сервере
                app,    // имя приложения
                uri,    // запрошенный URI
                ip,     // IP клиента
                ts      // метка времени "yyyy-MM-dd HH:mm:ss"
        );

        try {
            // Явно указываем JSON — безопаснее при нестандартных конвертерах
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            rt.postForLocation(baseUrl + "/hit", new HttpEntity<>(dto, headers));
        } catch (Exception e) {
            // Логируем и продолжаем работу основного сервиса
            log.warn("[StatsClient] Ошибка при отправке /hit: {}", e.toString());
        }
    }

    @Override
    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique) {
        StringBuilder url = new StringBuilder()
                .append(baseUrl)
                .append("/stats?start=").append(enc(F.format(start)))
                .append("&end=").append(enc(F.format(end)))
                .append("&unique=").append(unique);

        // Передаём URIs повторяющимися параметрами для максимальной совместимости
        if (uris != null && !uris.isEmpty()) {
            for (String u : uris) {
                url.append("&uris=").append(enc(u));
            }
        }

        try {
            ResponseEntity<ViewStatsDto[]> resp =
                    rt.getForEntity(url.toString(), ViewStatsDto[].class);
            ViewStatsDto[] body = resp.getBody();
            return Arrays.asList(body == null ? new ViewStatsDto[0] : body);
        } catch (Exception e) {
            log.warn("[StatsClient] Ошибка при получении /stats: {}", e.toString());
            return new ArrayList<>();
        }
    }

    // Определение IP клиента с учётом прокси
    private static String clientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    // Безопасное кодирование query-параметров
    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}