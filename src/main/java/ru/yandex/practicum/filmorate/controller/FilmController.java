package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;

@Slf4j
@RestController
public class FilmController {
    @Autowired
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

    @GetMapping("/genres")
    public Collection<Genre> findAllGenres() { return filmService.findAllGenres(); }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable int id) {
        if (filmService.findFilmById(id) == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        return filmService.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        if (filmService.findFilmById(id) == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        if (filmService.findFilmById(id) == null) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> findPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Requested " + count + " popular films");
        return filmService.findPopularFilms(count);
    }

    @GetMapping("/mpa/{id}")
    public MpaRating findMpaById(@PathVariable int id) {
       MpaRating mpa = filmService.findMpaById(id);
       if(mpa == null ){
           throw new NotFoundException("Mpa not found");
       }
       return mpa;
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable int id) {
        Genre genre = filmService.findGenreById(id);
        if(genre == null) {
            throw new NotFoundException("Genre not found");
        }
        return genre;
    }

    @GetMapping("/mpa")
    public Collection<MpaRating> findAllMpas() {
        return filmService.findAllMpas();
    }
}
