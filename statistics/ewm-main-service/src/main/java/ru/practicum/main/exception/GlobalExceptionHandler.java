package ru.practicum.main.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

/* # Глобальный обработчик исключений: корректные коды вместо 500 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* # 404: сущность не найдена */
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

    /* # 404: неизвестный путь (нет обработчика/ресурса) */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoResource(NoResourceFoundException ex) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("Not Found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* # 405: неверный HTTP-метод */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return ApiError.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.name())
                .reason("Method Not Allowed")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* # 409: конфликт/уникальность/бизнес-правила */
    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(RuntimeException ex) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Conflict")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* # 400: ошибки валидации и некорректные параметры/тела */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(Exception ex) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Bad Request")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleOthers(Throwable ex) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Internal Server Error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}