package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    void addUser(User user);

    Collection<User> findUserFriends(int id);

    User findUserById(int id);

    void deleteUser(int id);

    void updateUser(User user);

    Collection<User> findAllUsers();


}
