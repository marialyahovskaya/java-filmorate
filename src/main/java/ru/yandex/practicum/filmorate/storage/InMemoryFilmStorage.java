package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer, Film> films = new HashMap<>();
    private final Set<String> likes = new HashSet<>();
    // private final HashMap<Integer, Set<Integer>> likes = new HashMap<>();

    @Override
    public void addFilm(Film film){
        films.put(film.getId(), film);
    }

    @Override
    public Film findFilmById(int id) {
        return films.get(id);
    }

    @Override
    public void deleteFilm(int id) {
      films.remove(id);
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    public Collection<Film> findAllFilms(){
        return films.values();
    }

}
