package ru.practicum.main.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(RuntimeException ex) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Conflict")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIllegalState(IllegalStateException ex) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Illegal state")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnknown(Exception ex) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Internal error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}