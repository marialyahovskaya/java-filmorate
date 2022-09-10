package ru.yandex.practicum.filmorate.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import static java.time.Month.DECEMBER;

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
        if (film.getDuration() < 0) {
            log.info("Film's duration can't be negative");
            throw new ValidationException("Film's duration can't be negative");
        }
        if (film.getName() == null || film.getName().equals("")) {
            log.info("Film's name is incorrect.");
            throw new ValidationException("Film's name is incorrect.");
        }
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, DECEMBER, 28))) {
            log.info("Film couldn't be released that date");
            throw new ValidationException("Film couldn't be released that date");
        }
        if (film.getDescription().length() > 200) {
            log.info("Description is too long.");
            throw new ValidationException("Description is too long.");
        }

        film.setId(generateId());
        films.put(film.getId(), film);
        log.info(film.toString());
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.info("Film not found");
            throw new ValidationException("Film not found");
        }
        if (film.getDuration() < 0) {
            log.info("Film's duration can't be negative");
            throw new ValidationException("Film's duration can't be negative");
        }
        if (film.getName() == null || film.getName().equals("")) {
            log.info("Film's name is incorrect.");
            throw new ValidationException("Film's name is incorrect.");
        }
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, DECEMBER, 28))) {
            log.info("Film couldn't be released that date");
            throw new ValidationException("Film couldn't be released that date");
        }
        if (film.getDescription().length() > 200) {
            log.info("Description is too long.");
            throw new ValidationException("Description is too long.");
        }

        films.put(film.getId(), film);
        log.info(film.toString());
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return films.values();
    }
}
