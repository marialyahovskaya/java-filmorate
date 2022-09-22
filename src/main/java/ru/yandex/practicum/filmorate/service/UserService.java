package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private int nextId = 0;
    private UserStorage userStorage;

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

    public void addUser(final User user) {
        user.setId(generateId());
        userStorage.addUser(user);
    }

    public User updateUser(final User user){
        userStorage.updateUser(user);
        return user;
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public Collection<User> findAllUsers(){
        return userStorage.findAllUsers();
    }

}
