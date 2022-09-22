package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;

@Slf4j
@RestController
public class FilmController {
    private FilmService filmService;

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);

        filmService.addFilm(film);
        log.info(film.toString());
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        if (filmService.findFilmById(film.getId()) == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        FilmValidator.validate(film);
        filmService.updateFilm(film);
        log.info(film.toString());
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(int id, int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(int id, int userId) {
        filmService.deleteLike(id, userId);
    }
}
