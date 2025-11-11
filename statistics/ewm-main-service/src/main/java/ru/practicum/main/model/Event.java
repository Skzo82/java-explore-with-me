package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/* # Сущность события: поля синхронизированы с DTO и маппером */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;
    private String description;

    /* Важно: name=title, сеттер/геттер есть через Lombok */
    @Column(name = "title")
    private String title;

    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;

    /* boolean -> Lombok сгенерирует isPaid()/isRequestModeration() */
    private boolean paid;
    private boolean requestModeration;

    private int participantLimit;
    private int views;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /* Храним координаты в отдельных колонках */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
    })
    private Location location;
}