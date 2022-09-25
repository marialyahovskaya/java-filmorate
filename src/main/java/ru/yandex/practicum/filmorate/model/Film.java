package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class Film implements Comparable<Film> {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    final private Set<Integer> likes = new HashSet<>();

    public void addLike(final int userId) {
        likes.add(userId);
    }

    public void deleteLike(final int userId) {
        likes.remove(userId);
    }

    @Override
    public int compareTo(Film other) {
         return other.likes.size() - this.getLikes().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(likes, film.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, likes);
    }
}
