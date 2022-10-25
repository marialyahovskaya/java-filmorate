package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film findFilmById(final int id) {
        return filmStorage.findFilmById(id);
    }

    public MpaRating findMpaById(final int id) {
        return filmStorage.findMpaById(id);
    }

    public Genre findGenreById(final int id) {
        return filmStorage.findGenreById(id);
    }


    public void addFilm(final Film film) {
        int id = filmStorage.addFilm(film);
        film.setId(id);
    }

    public Film updateFilm(final Film film) {
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        filmStorage.updateFilm(film);
        return film;
    }

    public void deleteFilm(final int id) {
        filmStorage.deleteFilm(id);
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Collection<Genre> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    public Collection<MpaRating> findAllMpas() {
        return filmStorage.findAllMpas();
    }

    public void addLike(final int filmId, final int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(final int filmId, final int userId) {
        final Film film;
        film = filmStorage.findFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("This user never liked this film in the first place");
        }
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> findPopularFilms(final int count) {
        return filmStorage.findPopularFilms(count);
    }
}
