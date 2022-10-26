package ru.yandex.practicum.filmorate.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(final String message) {
        super(message);
    }
}
