package ru.practicum.main.exception;

/* # Бизнес-исключение "не найдено" */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}