package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;

/* # Категория событий */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* # Название категории (уникальное) */
    @Column(nullable = false, unique = true, length = 128)
    private String name;
}