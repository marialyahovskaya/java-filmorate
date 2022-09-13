package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {
    private User.UserBuilder builder;
    private User user;

    @BeforeEach
    public void beforeEach() {
        builder = User.builder();
    }

    @Test
    public void shouldSuccessfullyValidateCorrectUser() {
        user = builder.email("User123@rambler.ru").login("PeterIvanov").name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        user = builder.email("").login("PeterIvanov").name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNull() {
        user = builder.email(null).login("PeterIvanov").name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsWithoutAtSymbol() {
        user = builder.email("User123rambler.ru").login("PeterIvanov").name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmpty() {
        user = builder.email("User123@rambler.ru").login("").name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsNull() {
        user = builder.email("User123@rambler.ru").login(null).name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginContainsSpace() {
        user = builder.email("User123@rambler.ru").login("g g").name("Peter")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayIsInFuture() {
        user = builder.email("User123@rambler.ru").login("PeterIvanov").name("Peter")
                .birthday(LocalDate.now().plusDays(1)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldNotThrowExceptionWhenBirthdayIsToday() {
        user = builder.email("User123@rambler.ru").login("PeterIvanov").name("Peter")
                .birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }
}

