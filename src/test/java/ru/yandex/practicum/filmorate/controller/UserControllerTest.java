package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private User.UserBuilder builder;
    private UserController userController;
    private User user;

    @BeforeEach
    public void beforeEach() {
        builder = User.builder();
        userController = new UserController();
    }

    @Test
    public void shouldUseLoginAsANameWhenNameIsEmpty() {
        user = builder.email("User123@rambler.ru").login("PeterIvanov").name("")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertDoesNotThrow(() -> {
            user = userController.create(user);
            assertEquals("PeterIvanov", user.getName());
        });
    }

    @Test
    public void shouldUseLoginAsANameWhenNameIsNull() {
        user = builder.email("User123@rambler.ru").login("PeterIvanov").name(null)
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertDoesNotThrow(() -> {
            user = userController.create(user);
            assertEquals("PeterIvanov", user.getName());
        });
    }
}

