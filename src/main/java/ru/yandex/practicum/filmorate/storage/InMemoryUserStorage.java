package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(final User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findUserById(final int id) {
        return users.get(id);
    }

    @Override
    public void deleteUser(final int id) {
        users.remove(id);
    }

    @Override
    public void updateUser(final User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public Collection<User> findUserFriends(final int id) {
        final Set<Integer> idFriends = users.get(id).getFriends();
        final List<User> userFriends = new ArrayList<>();
        for (final int idFriend : idFriends) {
            userFriends.add(findUserById(idFriend));
        }
        return userFriends;
    }
}
