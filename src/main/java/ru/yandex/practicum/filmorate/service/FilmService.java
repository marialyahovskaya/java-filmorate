package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {

    private int nextId = 0;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private int generateId() {
        return ++nextId;
    }

    public Film findFilmById(final int id) {
        return filmStorage.findFilmById(id);
    }

    public void addFilm(final Film film) {
        film.setId(generateId());
        filmStorage.addFilm(film);
    }

    public Film updateFilm(final Film film) {
        filmStorage.updateFilm(film);
        return film;
    }

    public void deleteFilm(final int id) {
        filmStorage.deleteFilm(id);
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public void addLike(final int id, final int userId) {
        final Film film;
        film = filmStorage.findFilmById(id);
        film.addLike(userId);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(final int id, final int userId) {
        final Film film;
        film = filmStorage.findFilmById(id);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("This user never liked this film in the first place");
        }
        film.deleteLike(userId);
    }

    public Collection<Film> findPopularFilms(final int count) {
        return filmStorage.findPopularFilms(count);
    }

}
