package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;


public interface FilmStorage {

    public void addFilm(Film film);

    public Film findFilmById(int id);

    public void deleteFilm(int id);

    public void updateFilm(Film film);

    public Collection<Film> findAllFilms();

}
