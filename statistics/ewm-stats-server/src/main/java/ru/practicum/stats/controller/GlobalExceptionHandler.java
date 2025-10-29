package ru.practicum.stats.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // простой ответ об ошибке для автотестов
    record ErrorResponse(String status, String reason, String message, String timestamp) {
    }

    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        var body = new ErrorResponse(
                "BAD_REQUEST",
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now().format(F)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
