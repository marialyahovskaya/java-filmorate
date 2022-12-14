package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {
    protected final HashMap<Integer, User> users = new HashMap<>();

    private int nextId = 0;

    private int generateId() {
        return ++nextId;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info(user.toString());
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) throws ValidationException, NotFoundException {
        if (!users.containsKey(user.getId())) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info(user.toString());
        return user;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return users.values();
    }
}
