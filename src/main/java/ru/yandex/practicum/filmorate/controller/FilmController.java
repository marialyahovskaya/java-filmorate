package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    protected final HashMap<Integer, Film> films = new HashMap<>();

    private int nextId = 0;

    private int generateId() {
        return ++nextId;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info(film.toString());
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) throws ValidationException, NotFoundException {
        if (!films.containsKey(film.getId())) {
            log.info("Film not found");
            throw new NotFoundException("Film not found");
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info(film.toString());
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return films.values();
    }
}
