package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;


public interface FilmStorage {

    int addFilm(Film film);

    Film findFilmById(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    MpaRating findMpaById(int id);

    Genre findGenreById(int id);

    void deleteFilm(int id);

    void updateFilm(Film film);

    Collection<Film> findAllFilms();

    Collection<Genre> findAllGenres();

    Collection<MpaRating> findAllMpas();

    Collection<Film> findPopularFilms(int count);
}
