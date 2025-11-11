package ru.practicum.main.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* # В БД как FLOAT, в DTO как Double -> конвертация в маппере */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}