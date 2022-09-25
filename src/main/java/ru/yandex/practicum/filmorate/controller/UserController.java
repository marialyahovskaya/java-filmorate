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

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable int id) {
        if (userService.findUserById(id) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        return userService.findUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> findUserFriends(@PathVariable int id) {
        if (userService.findUserById(id) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        return userService.findUserFriends(id);
    }


    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userService.findUserById(id) == null || userService.findUserById(friendId) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (userService.findUserById(id) == null || userService.findUserById(otherId) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        return userService.findCommonFriends(id, otherId);
    }
}
