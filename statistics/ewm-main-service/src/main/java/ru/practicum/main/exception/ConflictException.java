package ru.practicum.main.exception;

/* # Конфликт бизнес-правил / состояния -> HTTP 409 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}