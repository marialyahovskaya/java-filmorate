package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    int addUser(User user);

    void addFriendship(int userId, int friendId);

    Collection<User> findUserFriends(int id);

    User findUserById(int id);

    void deleteUser(int id);

    void updateUser(User user);

    Collection<User> findAllUsers();

    void deleteFriend(int userId, int friendId);
}
