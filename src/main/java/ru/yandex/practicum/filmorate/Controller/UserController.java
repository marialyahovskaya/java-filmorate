package ru.yandex.practicum.filmorate.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
        if (user.getEmail() == null) {
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
        if (user.getName() == null) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        if (!user.getBirthday().isBefore(LocalDate.now())) {
            log.info("Birthday can't be in future");
            throw new ValidationException("Birthday can't be in future.");
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        log.info(user.toString());
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.info("User not found");
            throw new ValidationException("User not found");
        }
        if (user.getEmail() == null) {
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
        if (user.getName() == null) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        if (!user.getBirthday().isBefore(LocalDate.now())) {
            log.info("Birthday can't be in future");
            throw new ValidationException("Birthday can't be in future.");
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
