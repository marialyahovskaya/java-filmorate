package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Film findFilmById(final int id) {
        Film film = filmStorage.findFilmById(id);
        if (film == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    public MpaRating findMpaById(final int id) {
        MpaRating mpa = filmStorage.findMpaById(id);
        if (mpa == null) {
            throw new NotFoundException("Mpa not found");
        }
        return mpa;
    }

    public Genre findGenreById(final int id) {
        Genre genre = filmStorage.findGenreById(id);
        if (genre == null) {
            throw new NotFoundException("Genre not found");
        }
        return genre;
    }


    public Film addFilm(final Film film) {
        FilmValidator.validate(film);
        int id = filmStorage.addFilm(film);
        film.setId(id);
        return film;
    }

    public Film updateFilm(final Film film) {
        if (findFilmById(film.getId()) == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        FilmValidator.validate(film);
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
        if (filmStorage.findFilmById(filmId) == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(final int filmId, final int userId) {
        final Film film;
        film = filmStorage.findFilmById(filmId);
        if (film == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("This user never liked this film in the first place");
        }
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> findPopularFilms(final int count) {
        return filmStorage.findPopularFilms(count);
    }
}
