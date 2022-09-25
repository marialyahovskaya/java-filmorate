package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;


public interface FilmStorage {

    void addFilm(Film film);

    Film findFilmById(int id);

    void deleteFilm(int id);

    void updateFilm(Film film);

    Collection<Film> findAllFilms();

    Collection<Film> findPopularFilms(int count);

}
