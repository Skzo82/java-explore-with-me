package ru.practicum.main.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/* # Унифицированный ответ об ошибке */
@Getter
@Builder
@AllArgsConstructor
public class ApiError {
    private final String message;     // # сообщение об ошибке
    private final String reason;      // # причина
    private final String status;      // # HTTP-статус как строка
    private final LocalDateTime timestamp; // # время возникновения
}