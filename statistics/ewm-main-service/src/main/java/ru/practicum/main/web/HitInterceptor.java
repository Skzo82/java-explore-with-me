package ru.practicum.main.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.practicum.stats.client.StatsClient;

/* # Перехватчик для записи хитов в сервис статистики */
@Slf4j
@Component
@RequiredArgsConstructor
public class HitInterceptor implements HandlerInterceptor {

    /* # Клиент статистики (HTTP) */
    private final StatsClient statsClient;

    /* # Имя приложения для сохранения в статистике */
    @Value("${stats.app-name:ewm-main-service}")
    private String appName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // # Не пишем хиты для служебных эндпоинтов
            String uri = request.getRequestURI();
            if (uri.startsWith("/actuator") || uri.equals("/error")) {
                return true;
            }

            // # Отправляем хит в сервис статистики
            statsClient.saveHit(request, appName);
        } catch (Exception ex) {
            // # Логируем, но не прерываем обработку запроса
            log.warn("Не удалось записать хит в сервис статистики: {}", ex.getMessage());
        }
        return true;
    }
}