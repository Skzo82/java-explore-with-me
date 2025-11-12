package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

/* # Сущность подборки событий */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // # идентификатор

    @Column(nullable = false, length = 50)
    private String title; // # заголовок

    @Column(nullable = false)
    @Builder.Default
    private Boolean pinned = false; // # закреплена ли на главной

    /* # связь с событиями через промежуточную таблицу */
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> events = new LinkedHashSet<>();
}