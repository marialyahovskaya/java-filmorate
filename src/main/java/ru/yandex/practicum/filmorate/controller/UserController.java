package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        userService.addUser(user);
        log.info(user.toString());
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        if (userService.findUserById(user.getId()) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        userService.updateUser(user);
        log.info(user.toString());
        return user;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.findAllUsers();
    }
}
