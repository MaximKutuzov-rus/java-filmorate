package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @GetMapping
    public Collection<User> allUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user != null) {
            if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.error("Ошибка валидации, имейл пустой или без знака @");
                throw new ValidationException("Имейл не может быть пустой строкой и должен содержать знак @");
            }
            if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
                log.error("Ошибка валидации, логин пустой или содержит пробелы");
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                log.info("Имя заменено на логин");
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Ошибка валидации, дата рождения в будущем");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }

            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Пользователь успешно добавлен");
            log.info(user.toString());

            return user;
        } else {
            log.error("Ошибка валидации, пустой ввод");
            throw new ValidationException("Не передано никаких сведений о пользователе");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user != null) {
            if (users.containsKey(user.getId())) {
                if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                    log.error("Ошибка валидации, имейл без знака @");
                    throw new ValidationException("Имейл не может быть пустой строкой и должен содержать знак @");
                }
                if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
                    log.error("Ошибка валидации, логин пустой или содержит пробелы");
                    throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                }
                if (user.getName().isBlank()) {
                    log.info("Имя заменено на логин");
                    user.setName(user.getLogin());
                }
                if (user.getBirthday().isAfter(LocalDate.now())) {
                    log.error("Ошибка валидации, дата рождения в будущем");
                    throw new ValidationException("Дата рождения не может быть в будущем");
                }

                users.put(user.getId(), user);
                log.info("Данные пользователя успешно обновлены");


                return user;
            } else {
                log.error("Ошибка валидации, пользователь не найден");
                throw new ValidationException("Пользователь не найден");
            }
        } else {
            log.error("Ошибка валидации, пустой ввод");
            throw new ValidationException("Не передано никаких сведений о пользователе");
        }
    }
}
