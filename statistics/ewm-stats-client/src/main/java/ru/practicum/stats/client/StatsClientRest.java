package ru.practicum.stats.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsClientRest implements StatsClient {

    private final RestTemplate restTemplate;

    /* # Формат дат, который ждёт stats-service */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* # Базовый URL stats-сервиса (совпадает с docker-compose) */
    @Value("${stats-server.url:http://stats-server:9090}")
    private String baseUrl;

    @Override
    public void saveHit(HttpServletRequest request, String app) {
        // # Текущее время форматируем в строку нужного формата
        String ts = FORMATTER.format(LocalDateTime.now());

        EndpointHitDto hit = new EndpointHitDto(
                null,                      // id — сгенерирует stats-server
                app,                       // название приложения
                request.getRequestURI(),   // URI запроса
                request.getRemoteAddr(),   // IP клиента
                ts                         // строковое представление времени
        );

        try {
            restTemplate.postForEntity(baseUrl + "/hit", hit, Void.class);
        } catch (RestClientException ex) {
            // # Ошибка статистики не должна ломать основной сервис
            log.warn("Failed to send hit to stats service: {}", ex.getMessage());
        }
    }

    @Override
    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, boolean unique) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("start", toString(start));
        params.add("end", toString(end));
        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                params.add("uris", uri);
            }
        }
        params.add("unique", String.valueOf(unique));

        String url = baseUrl + "/stats" + buildQuery(params);

        try {
            ResponseEntity<ViewStatsDto[]> response =
                    restTemplate.getForEntity(url, ViewStatsDto[].class);

            ViewStatsDto[] body = response.getBody();
            if (body == null || body.length == 0) {
                return Collections.emptyList();
            }
            return List.of(body);
        } catch (RestClientException ex) {
            // # Если stats-service упал → просто логируем и возвращаем пустой список
            log.warn("Failed to get stats from stats service: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    /* # Преобразование Instant → строка нужного формата */
    private String toString(Instant instant) {
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return FORMATTER.format(ldt);
    }

    /* # Примитивный билдер query-строки */
    private String buildQuery(MultiValueMap<String, String> params) {
        StringBuilder sb = new StringBuilder("?");
        boolean first = true;
        for (var entry : params.entrySet()) {
            for (String value : entry.getValue()) {
                if (!first) {
                    sb.append('&');
                }
                first = false;
                sb.append(entry.getKey()).append('=').append(value);
            }
        }
        return sb.toString();
    }
}