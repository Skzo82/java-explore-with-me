package ru.practicum.main.exception;

/* # Бизнес-исключение "конфликт" → HTTP 409 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}