package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User findUserById(final int id) {
        User user = userStorage.findUserById(id);
        if (user == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public Collection<User> findUserFriends(final int id) {
        if (userStorage.findUserById(id) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        return userStorage.findUserFriends(id);
    }

    public User addUser(final User user) {
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        int id = userStorage.addUser(user);
        user.setId(id);
        return user;
    }

    public User updateUser(final User user) {
        if (userStorage.findUserById(user.getId()) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Login is instead of name");
            user.setName(user.getLogin());
        }
        userStorage.updateUser(user);
        return user;
    }

    public void deleteUser(final int id) {
        userStorage.deleteUser(id);
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public void addFriend(final int userId, final int friendId) {
        if (userStorage.findUserById(userId) == null || userStorage.findUserById(friendId) == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        userStorage.addFriendship(userId, friendId);
    }

    public void deleteFriend(final int userId, final int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> findCommonFriends(final int id, final int otherId) {
        User user = userStorage.findUserById(id);
        User otherUser = userStorage.findUserById(otherId);
        if (user == null || otherUser == null) {
            log.info("User not found");
            throw new NotFoundException("User not found");
        }
        final Set<User> result = new HashSet<>();
        result.addAll(user.getFriends());
        result.retainAll(otherUser.getFriends());

        return result;
    }
}
