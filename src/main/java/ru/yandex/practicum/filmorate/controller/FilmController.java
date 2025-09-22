package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @GetMapping
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (film != null) {
            if (film.getName() == null || film.getName().isBlank()) {
                log.error("Ошибка валидации, неправильный ввод имени");
                throw new ValidationException("Название фильма не может быть пустой строкой");
            }
            if (film.getDescription().length() > 200) {
                log.error("Ошибка валидации, превышение длины описания");
                throw new ValidationException("Длина описания фильма не должна превышать 200 символов");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Ошибка валидации, дата раньше 28 декабря 1895 года");
                throw new ValidationException("Дата выхода фильма должна быть позже 28 декабря 1895 года");
            }
            if (film.getDuration() < 0) {
                log.error("Ошибка валидации, отрицательная продолжительность фильма");
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }

            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм");
            log.info(film.toString());

            return film;
        } else {
            log.error("Ошибка валидации, пустой ввод");
            throw new ValidationException("Не передано никаких сведений о фильме");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film != null) {
            if (films.containsKey(film.getId())) {
                if (film.getName() == null || film.getName().isBlank()) {
                    log.error("Ошибка валидации, неправильный ввод имени");
                    throw new ValidationException("Название фильма не может быть пустой строкой");
                }
                if (film.getDescription().length() > 200) {
                    log.error("Ошибка валидации, превышение длины описания");
                    throw new ValidationException("Длина описания фильма не должна превышать 200 символов");
                }
                if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                    log.error("Ошибка валидации, дата раньше 28 декабря 1895 года");
                    throw new ValidationException("Дата выхода фильма должна быть позже 28 декабря 1895 года");
                }
                if (film.getDuration() < 0) {
                    log.error("Ошибка валидации, отрицательная продолжительность фильма");
                    throw new ValidationException("Продолжительность фильма должна быть положительной");
                }

                films.put(film.getId(), film);
                log.info("Данные фильма успешно обновлены");

                return film;
            } else {
                log.error("Ошибка валидации, фильм не найден");
                throw new ValidationException("Не найден такой фильм");
            }
        } else {
            log.error("Ошибка валидации, пустой ввод");
            throw new ValidationException("Не передано никаких сведений о фильме");
        }
    }
}
