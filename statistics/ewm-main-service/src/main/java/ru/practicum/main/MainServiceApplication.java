package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* # Включаем сканирование пакета клиента статистики */
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.main",
        "ru.practicum.stats.client"
})
public class MainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }
}