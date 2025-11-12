package ru.practicum.main.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/* # Глобальный обработчик ошибок: приводит все к ожидаемым кодам */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* # 404 Not Found */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException ex) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("Not Found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* # 409 Conflict: нарушения статуса/состояния и уникальностей */
    @ExceptionHandler({IllegalStateException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(RuntimeException ex) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Conflict")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* # 400 Bad Request: валидация/некорректные аргументы */
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(Exception ex) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Bad request")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* # 500: на всякий случай — чтобы Postman всегда получал JSON */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAny(Exception ex) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Internal error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}