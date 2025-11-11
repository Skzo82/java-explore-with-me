package ru.practicum.main.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/* # Унифицированный ответ об ошибке */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}