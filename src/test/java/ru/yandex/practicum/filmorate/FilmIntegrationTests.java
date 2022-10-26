package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class FilmIntegrationTests {

    private final FilmService filmService;
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach() throws IOException {
        File file = ResourceUtils.getFile("classpath:schema.sql");
        String sqlQuery = new String(Files.readAllBytes(file.toPath()));
        jdbcTemplate.update(sqlQuery);
        User user = User.builder()
                .name("Peter")
                .email("peter.peter@rambler.ru")
                .login("peter.peter")
                .birthday(LocalDate.of(1988, 12, 13)).build();
        userService.addUser(user);

        Film film = Film.builder().name("Film")
                .description("About film")
                .releaseDate(LocalDate.of(1990, 4, 1))
                .duration(38)
                .mpa(MpaRating.builder().id(1).build()).build();
        filmService.addFilm(film);

        film = Film.builder().name("Film2")
                .description("About film2")
                .releaseDate(LocalDate.of(1991, 4, 1))
                .duration(48)
                .mpa(MpaRating.builder().id(1).build()).build();
        filmService.addFilm(film);
    }

    @Test
    public void testAddFilm() {
        Film film = Film.builder().name("Film3")
                .description("About film3")
                .releaseDate(LocalDate.of(1992, 4, 1))
                .duration(58)
                .mpa(MpaRating.builder().id(2).build()).build();

        filmService.addFilm(film);
        Film film2 = filmService.findFilmById(3);
        assertEquals(film.getName(), film2.getName());
        assertEquals(film.getDescription(), film2.getDescription());
        assertEquals(film.getReleaseDate(), film2.getReleaseDate());
        assertEquals(film.getDuration(), film2.getDuration());
        assertEquals(film.getMpa().getId(), film2.getMpa().getId());
    }

    @Test
    public void testAddLike() {
        filmService.addLike(2, 1);
        Collection<Film> popularFilms = filmService.findPopularFilms(1);
        assertNotNull(popularFilms);
        assertEquals(1, popularFilms.size());
        Film film = popularFilms.iterator().next();
        assertEquals(2, film.getId());
    }

    @Test
    public void testDeleteLike() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.deleteLike(1, 1);
        Collection<Film> popularFilms = filmService.findPopularFilms(1);
        assertNotNull(popularFilms);
        assertEquals(1, popularFilms.size());
        Film film = popularFilms.iterator().next();
        assertEquals(2, film.getId());
    }

    @Test
    public void testFindMpaById() {
        MpaRating mpa = filmService.findMpaById(1);
        assertNotNull(mpa);
        assertEquals(1, mpa.getId());
        assertEquals("G", mpa.getName());
    }

    @Test
    public void testFindGenreById() {
        Genre genre = filmService.findGenreById(1);
        assertNotNull(genre);
        assertEquals(1, genre.getId());
        assertTrue(genre.getName().equals("Comedy") || genre.getName().equals("Комедия"));
    }

    @Test
    public void testDeleteFilm() {
        filmService.deleteFilm(1);
        Collection<Film> allFilms = filmService.findAllFilms();
        assertNotNull(allFilms);
        assertEquals(1, allFilms.size());
        Film film = allFilms.iterator().next();
        assertEquals(2, film.getId());
    }

    @Test
    public void testUpdateFilm() {
        Film film = Film.builder().id(1)
                .name("Film4")
                .description("About film4")
                .releaseDate(LocalDate.of(1992, 4, 1))
                .duration(68)
                .mpa(MpaRating.builder().id(2).build()).build();
        filmService.updateFilm(film);
        Film film2 = filmService.findFilmById(1);

        assertEquals(film.getName(), film2.getName());
        assertEquals(film.getDescription(), film2.getDescription());
        assertEquals(film.getReleaseDate(), film2.getReleaseDate());
        assertEquals(film.getDuration(), film2.getDuration());
        assertEquals(film.getMpa().getId(), film2.getMpa().getId());
    }

    @Test
    public void testFindAllFilms() {
        Collection<Film> allFilms = filmService.findAllFilms();
        assertEquals(2, allFilms.size());
    }

    @Test
    public void testFindAllGenres() {
        Collection<Genre> allGenres = filmService.findAllGenres();
        assertEquals(6, allGenres.size());
    }

    @Test
    public void testFindAllMpas() {
        Collection<MpaRating> allMpas = filmService.findAllMpas();
        assertEquals(5, allMpas.size());
    }
}
