package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public void addFilm(final Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Film findFilmById(final int id) {
        return films.get(id);
    }

    @Override
    public void deleteFilm(final int id) {
        films.remove(id);
    }

    @Override
    public void updateFilm(final Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Collection<Film> findPopularFilms(final int count) {
        final List<Film> popularFilms = new ArrayList<>(films.values());
        Collections.sort(popularFilms);
        if (count < popularFilms.size()) {
            return popularFilms.subList(0, count);
        } else {
            return popularFilms;
        }
    }
}
