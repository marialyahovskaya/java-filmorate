package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    public void addUser(User user);

//    public User findUserByEmail(String email);

    public User findUserById(int id);

    public void deleteUser(int id);

    public void updateUser(User user);

    public Collection<User> findAllUsers();

}
