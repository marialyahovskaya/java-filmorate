package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
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
class UserIntegrationTests {
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

        user = User.builder()
                .name("Max")
                .email("max.max@rambler.ru")
                .login("max.max")
                .birthday(LocalDate.of(1985, 1, 3)).build();
        userService.addUser(user);
    }

    @Test
    public void testFindUserById() {

        User user = userService.findUserById(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    public void testAddUser() {
        User user = nick();

        userService.addUser(user);
        User user2 = userService.findUserById(3);
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getLogin(), user2.getLogin());
        assertEquals(user.getBirthday(), user2.getBirthday());
    }

    @Test
    public void testAddFriendship() {
        userService.addFriend(1, 2);
        Collection<User> friends = userService.findUserFriends(1);
        assertNotNull(friends);
        assertEquals(1, friends.size());
        User friend = friends.iterator().next();
        assertEquals(2, friend.getId());
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(1);
        assertThrows(NotFoundException.class, () -> {
            userService.findUserById(1);
        });
    }

    @Test
    public void testUpdateUser() {
        User user = nick();
        user.setId(1);

        userService.updateUser(user);
        User user2 = userService.findUserById(1);
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getLogin(), user2.getLogin());
        assertEquals(user.getBirthday(), user2.getBirthday());
    }

    @Test
    public void testFindAllUsers() {
        Collection<User> allUsers = userService.findAllUsers();
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }

    @Test
    public void testDeleteFriend() {
        userService.addFriend(1, 2);
        userService.deleteFriend(1, 2);
        Collection<User> friends = userService.findUserFriends(1);
        assertEquals(0, friends.size());
    }

    private User nick() {
        return User.builder()
                .name("Nick")
                .email("nick.nick@rambler.ru")
                .login("nick.nick")
                .birthday(LocalDate.of(1989, 10, 16)).build();
    }
}