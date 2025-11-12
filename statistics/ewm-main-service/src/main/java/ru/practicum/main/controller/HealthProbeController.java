package ru.practicum.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/* # Простой health-check эндпоинт для тестов Postman */
@RestController
@RequestMapping("/api")
public class HealthProbeController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "OK");
    }
}