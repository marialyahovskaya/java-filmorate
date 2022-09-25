package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;

@Slf4j
public class FilmValidator {
    public static void validate(final Film film) throws ValidationException {
        if (film.getDuration() <= 0) {
            log.info("Film duration must be positive number");
            throw new ValidationException("Film duration must be positive number");
        }
        if (film.getName() == null || film.getName().equals("")) {
            log.info("Film name is incorrect.");
            throw new ValidationException("Film name is incorrect");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            log.info("Film couldn't be released that date");
            throw new ValidationException("Film couldn't be released that date");
        }
        if (film.getDescription().length() > 200) {
            log.info("Description is too long");
            throw new ValidationException("Description is too long");
        }
    }
}
