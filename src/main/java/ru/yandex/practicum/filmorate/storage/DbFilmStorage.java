package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQuery = "insert into film_genres(film_id, genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }
        return filmId;
    }

    private Collection<Integer> findLikesByFilmId(int id) {
        String sqlQuery = "SELECT l.*\n" +
                "FROM LIKES l \n" +
                "INNER JOIN FILMS f ON f.ID = l.FILM_ID \n" +
                "WHERE l.FILM_ID = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("user_id"), id);
    }

    public Film findFilmById(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        if (rs.next()) {
            Film.FilmBuilder builder = Film.builder();
            builder.id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(findMpaById(rs.getInt("mpa_rating_id")))
                    .genres(findGenresByFilmId(rs.getInt("id")))
                    .likes(new HashSet<>(findLikesByFilmId(rs.getInt("id"))));
            log.info(builder.build().toString());
            return builder.build();
        } else {
            return null;
        }
    }

    private Collection<Genre> findGenresByFilmId(int id) {
        String sqlQuery = "SELECT g.*\n" +
                "FROM GENRES g \n" +
                "INNER JOIN FILM_GENRES fg ON fg.GENRE_ID = g.ID \n" +
                "WHERE fg.film_id = ?" +
                "ORDER BY g.id";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
    }

    public void deleteFilm(int id) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        if (film.getGenres() != null) {
            sqlQuery = "delete from film_genres where film_id = ?";

            jdbcTemplate.update(sqlQuery, film.getId());

            for (Genre genre : film.getGenres()) {
                sqlQuery = "insert into film_genres(film_id, genre_id) values (?, ?)";

                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }

        }
    }

    @Override
    public MpaRating findMpaById(final int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA_RATINGS where id = ?", id);
        if (mpaRows.next()) {
            MpaRating.MpaRatingBuilder builder = MpaRating.builder();
            builder.id(mpaRows.getInt("id"))
                    .name(mpaRows.getString("name"));

            return builder.build();
        } else {
            return null;
        }
    }

    @Override
    public Genre findGenreById(final int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRES where id = ?", id);
        if (genreRows.next()) {
            Genre.GenreBuilder builder = Genre.builder();
            builder.id(genreRows.getInt("id"))
                    .name(genreRows.getString("name"));

            return builder.build();
        } else {
            return null;
        }
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        Film.FilmBuilder builder = Film.builder();
        builder.id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(findMpaById(rs.getInt("mpa_rating_id")))
                .genres(findGenresByFilmId(rs.getInt("id")));

        return builder.build();
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sqlQuery = "select films.*\n" +
                "from films\n";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Collection<Genre> findAllGenres() {
        String sqlQuery = "select genres.*\n" +
                "from genres\n";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    private MpaRating makeMpa(ResultSet rs) throws SQLException {
        MpaRating.MpaRatingBuilder builder = MpaRating.builder();
        builder.id(rs.getInt("id"))
                .name(rs.getString("name"));

        return builder.build();
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre.GenreBuilder builder = Genre.builder();
        builder.id(rs.getInt("id"))
                .name(rs.getString("name"));

        return builder.build();
    }

    @Override
    public Collection<MpaRating> findAllMpas() {
        String sqlQuery = "select mpa_ratings.*\n" +
                "from mpa_ratings\n";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    public Collection<Film> findPopularFilms(int count) {
        String sqlQuery = "SELECT f.*\n" +
                "FROM FILMS f \n" +
                "LEFT JOIN likes AS l ON l.film_id = f.id \n" +
                "GROUP BY f.id\n" +
                "ORDER BY count(l.id) desc\n" +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into likes(film_id, user_id) " +
                "values (?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}
