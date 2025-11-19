package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;

/* # Пользователь доменная модель */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* # Уникальный email пользователя */
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    /* # Отображаемое имя */
    @Column(nullable = false, length = 128)
    private String name;
}