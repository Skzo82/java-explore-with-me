package ru.practicum.stats.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    // базовый ответ для ошибок валидации входных данных
    private Map<String, Object> errorBody(HttpStatus status, String message) {
        return Map.of(
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    // некорректная бизнес-логика (например, start > end)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        return errorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // неверный тип параметра (например, формат даты для LocalDateTime)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Invalid parameter '" + ex.getName() + "': " + ex.getValue();
        return errorBody(HttpStatus.BAD_REQUEST, msg);
    }

    // отсутствует обязательный параметр запроса
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Missing required parameter: " + ex.getParameterName();
        return errorBody(HttpStatus.BAD_REQUEST, msg);
    }

    // тело запроса не читается / некорректный JSON / неправильный timestamp в JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleUnreadable(HttpMessageNotReadableException ex) {
        String msg = "Invalid request body (check JSON and timestamp format)";
        return errorBody(HttpStatus.BAD_REQUEST, msg);
    }

    // любой прочий неперехваченный сценарий — чтобы не получить 500 без тела
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleOther(Exception ex) {
        return errorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}