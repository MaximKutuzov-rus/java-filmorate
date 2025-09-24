package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

class UserControllerTest {
    UserController controller = new UserController();

    @Test
    void allUsersTest() {
        User user1 = new User(null, "email@", "login", "name",
                LocalDate.of(2006, 1, 18));
        User user2 = new User(null, "email@", "login", "name",
                LocalDate.of(2007, 1, 18));
        User user3 = new User(null, "email@", "login", "name",
                LocalDate.of(2008, 1, 18));

        controller.addUser(user1);
        controller.addUser(user2);
        controller.addUser(user3);

        List<User> allFilms = controller.allUsers();
        Assertions.assertEquals(user1, allFilms.getFirst());
        Assertions.assertEquals(user3, allFilms.getLast());
    }

    @Test
    void addUserTest() {
        User user1 = new User(null, "email", "login", "name",
                LocalDate.of(2006, 1, 18));
        Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user1));

        User user2 = new User(null, "", "login", "name",
                LocalDate.of(2007, 1, 18));
        Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user2));

        User user3 = new User(null, "email@", "login 1", "name",
                LocalDate.of(2008, 1, 18));
        Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user3));

        User user4 = new User(null, "email@", "login1", "",
                LocalDate.of(2008, 1, 18));
        controller.addUser(user4);
        Assertions.assertEquals(user4.getLogin(), user4.getName());

        User user5 = new User(null, "email@", "login1", "",
                LocalDate.of(2026, 1, 18));
        Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user5));

        User user6 = null;
        Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user6));
    }

    @Test
    void updateUserTest() {
        User user1 = new User(null, "email@", "login", "name",
                LocalDate.of(2006, 1, 18));
        User user2 = new User(1L, "email@", "login", "name",
                LocalDate.of(2007, 1, 18));
        User user3 = new User(20L, "email@", "login", "name",
                LocalDate.of(2008, 1, 18));
        controller.addUser(user1);

        Assertions.assertThrows(ValidationException.class, () -> controller.updateUser(user3));
        Assertions.assertDoesNotThrow(() -> controller.updateUser(user2));
    }
}