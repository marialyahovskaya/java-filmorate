package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private int nextId = 0;
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        user.setId(generateId());
        userStorage.addUser(user);
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
        User user;
        user = userStorage.findUserById(userId);
        user.addFriend(friendId);
        userStorage.updateUser(user);
        user = userStorage.findUserById(friendId);
        user.addFriend(userId);
        userStorage.updateUser(user);
    }

    public void deleteFriend(final int userId, final int friendId) {
        final User user;
        user = userStorage.findUserById(userId);
        user.deleteFriend(friendId);
        userStorage.updateUser(user);
    }

    public Collection<User> findCommonFriends(final int id, final int otherId) {
        final Set<Integer> commonFriendsIds1;
        final Set<Integer> commonFriendsIds2;
        final Set<Integer> result = new HashSet<>();
        commonFriendsIds1 = userStorage.findUserById(id).getFriends();
        commonFriendsIds2 = userStorage.findUserById(otherId).getFriends();
        result.addAll(commonFriendsIds1);
        result.retainAll(commonFriendsIds2);
        List<User> commonFriends = new ArrayList<>();
        for (final int commonFriendsIds : result) {
            commonFriends.add(userStorage.findUserById(commonFriendsIds));
        }
        return commonFriends;
    }
}
