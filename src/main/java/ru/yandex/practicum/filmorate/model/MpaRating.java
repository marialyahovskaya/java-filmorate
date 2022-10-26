package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MpaRating {
    private int id;
    private String name;
}
