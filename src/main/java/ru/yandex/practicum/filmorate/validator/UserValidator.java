package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void validate(final User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().equals("")) {
            log.info("Email is empty");
            throw new ValidationException("Email is empty.");
        }
        if (!user.getEmail().contains("@")) {
            log.info("Email must contains symbol \"@\"");
            throw new ValidationException("Email must contains symbol \"@\".");
        }
        if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
            log.info("Login is incorrect");
            throw new ValidationException("Login is incorrect");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Birthday can't be in future");
            throw new ValidationException("Birthday can't be in future.");
        }
    }
}
