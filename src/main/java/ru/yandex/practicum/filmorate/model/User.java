package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    final private Set<Integer> friends = new HashSet<>();

    public void addFriend(final int userId) {
        friends.add(userId);
    }

    public void deleteFriend(final int userId) {
        friends.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, name, birthday, friends);
    }
}
