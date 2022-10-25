package ru.yandex.practicum.filmorate.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User.UserBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addUser(final User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public User findUserById(final int id) {

        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            UserBuilder builder = User.builder();
            builder.id(userRows.getInt("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .friends(new HashSet<>(findUserFriends(userRows.getInt("id"))));


            //    log.info("Найден пользователь: {} {}", user.getId(), user.getNickname());

            return builder.build();
        } else {
            //   log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public void deleteUser(final int id) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void updateUser(final User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ?  " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
    }

    private User makeUser(ResultSet rs) throws SQLException {
        UserBuilder builder = User.builder();
        builder.id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate());
        return builder.build();
    }

    @Override
    public Collection<User> findAllUsers() {
        String sqlQuery = "select users.*\n" +
                "from users\n";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Collection<User> findUserFriends(final int id) {
        String sqlQuery = "SELECT * FROM USERS\n" +
                "WHERE id IN (SELECT friend_id\n" +
                "FROM friendships\n" +
                "WHERE user_id = ?)";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public void addFriendship(int userId, int friendId) {
        String sqlQuery = "insert into friendships(user_id, friend_id) " +
                "values (?, ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from friendships where user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
