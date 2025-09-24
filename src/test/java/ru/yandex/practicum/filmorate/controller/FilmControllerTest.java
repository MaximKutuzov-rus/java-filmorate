package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {
    FilmController controller = new FilmController();

    @Test
    void allFilmsTest() {
        Film film1 = new Film(null, "name", "description",
                LocalDate.of(1978, 10, 4), 30);
        Film film2 = new Film(null, "name", "description",
                LocalDate.of(1978, 10, 4), 30);
        Film film3 = new Film(null, "name", "description",
                LocalDate.of(1979, 10, 4), 30);

        controller.addFilm(film1);
        controller.addFilm(film2);
        controller.addFilm(film3);

        List<Film> allFilms = controller.allFilms();
        Assertions.assertEquals(film1, allFilms.getFirst());
        Assertions.assertEquals(film3, allFilms.getLast());
    }

    @Test
    void addFilmTest() {
        Film film1 = new Film(null, "", "description",
                LocalDate.of(1978, 10, 4), 30);
        Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film1));

        Film film2 = new Film(null, null, "description",
                LocalDate.of(1978, 10, 4), 30);
        Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film2));

        Film film3 = new Film(null, "name", "descriptionjk;" +
                "llljk;lvewnopuweotpu4toiewtuweoitewutoiweutweoituetoireuteorituetoierute" +
                "roituretoreutoertureoituretoierutreotiuertoireutoretureoitreutoiertueroit" +
                "reutoiertureopituretoreitureoituretoireutreoitureoiterutopiertureopitreutoir" +
                "etureoituretoieruteortueorutoerwtureopitvuewro",
                LocalDate.of(1978, 10, 4), 30);
        Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film3));

        Film film4 = new Film(null, "name", "description",
                LocalDate.of(1878, 10, 4), 30);
        Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film4));

        Film film5 = new Film(null, "name", "description",
                LocalDate.of(1978, 10, 4), -30);
        Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film5));

        Film film6 = null;
        Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film6));
    }

    @Test
    void updateFilmTest() {
        Film film1 = new Film(null, "name", "description",
                LocalDate.of(1978, 10, 4), 30);
        Film film2 = new Film(20L, "name", "description",
                LocalDate.of(1980, 10, 4), 30);
        Film film3 = new Film(1L, "name", "description",
                LocalDate.of(1980, 10, 4), 30);
        controller.addFilm(film1);
        Assertions.assertThrows(ValidationException.class, () -> controller.updateFilm(film2));
        Assertions.assertDoesNotThrow(() -> controller.updateFilm(film3));
    }
}