package ru.practicum.main.model;

import jakarta.persistence.Embeddable;
import lombok.*;

/* # Координаты события (встраиваемый объект JPA) */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    /* # Широта (latitude) */
    private Double lat;

    /* # Долгота (longitude) */
    private Double lon;
}