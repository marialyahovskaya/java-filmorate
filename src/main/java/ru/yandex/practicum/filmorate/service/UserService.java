package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private int nextId = 0;
    private final UserStorage userStorage;

    @Autowired
    public UserService(DbUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private int generateId() {
        return ++nextId;
    }

    public User findUserById(final int id) {
        return userStorage.findUserById(id);
    }

    public Collection<User> findUserFriends(final int id) {
        return userStorage.findUserFriends(id);
    }

    public void addUser(final User user) {
        int id = userStorage.addUser(user);
        user.setId(id);
    }

    public User updateUser(final User user) {
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
       userStorage.addFriendship(userId, friendId);
    }

    public void deleteFriend(final int userId, final int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> findCommonFriends(final int id, final int otherId) {
        final Set<User> userFriends;
        final Set<User> otherUserFriends;
        final Set<User> result = new HashSet<>();
        userFriends = userStorage.findUserById(id).getFriends();
        otherUserFriends = userStorage.findUserById(otherId).getFriends();
        result.addAll(userFriends);
        result.retainAll(otherUserFriends);

        return result;
    }
}
