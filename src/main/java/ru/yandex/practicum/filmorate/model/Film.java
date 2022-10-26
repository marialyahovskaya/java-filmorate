package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film implements Comparable<Film> {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MpaRating mpa;
    private Collection<Genre> genres;
    private Set<Integer> likes;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("release_Date", releaseDate);
        map.put("duration", duration);
        map.put("mpa_rating_id", mpa.getId());
        return map;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, likes);
    }
}
