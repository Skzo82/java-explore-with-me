package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/* # Сущность события (основная таблица) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // идентификатор

    @Column(nullable = false, length = 2000)
    private String annotation; // краткое описание

    @Column(nullable = false, length = 7000)
    private String description; // полное описание

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate; // дата и время события

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn; // дата создания

    @Column(name = "published_on")
    private LocalDateTime publishedOn; // дата публикации

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // категория события

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator; // инициатор (автор)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EventState state; // PENDING / PUBLISHED / CANCELED

    @Embedded
    private Location location; // координаты (lat/lon)

    @Column(nullable = false)
    @Builder.Default
    private Boolean paid = false; // платное ли событие

    @Column(name = "participant_limit", nullable = false)
    @Builder.Default
    private Integer participantLimit = 0; // лимит участников

    @Column(name = "request_moderation", nullable = false)
    @Builder.Default
    private Boolean requestModeration = true; // требуется ли модерация заявок

    @Column(nullable = false)
    private String title; // заголовок

    @Column(nullable = false)
    @Builder.Default
    private Integer views = 0; // количество просмотров
}