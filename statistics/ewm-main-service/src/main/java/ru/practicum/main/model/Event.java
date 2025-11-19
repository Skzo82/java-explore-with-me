package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private EventState state = EventState.PENDING;

    @Embedded
    private Location location;

    @Column(nullable = false)
    @Builder.Default
    private Boolean paid = false;

    @Column(name = "participant_limit", nullable = false)
    @Builder.Default
    private Integer participantLimit = 0;

    @Column(name = "request_moderation", nullable = false)
    @Builder.Default
    private Boolean requestModeration = true;

    @Column(nullable = false)
    private String title;

    /* # Количество подтверждённых заявок — нужно тестам Postman */
    @Column(name = "confirmed_requests", nullable = false)
    @Builder.Default
    private Integer confirmedRequests = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer views = 0;

    @PrePersist
    void prePersist() {
        if (createdOn == null) createdOn = LocalDateTime.now();
        if (state == null) state = EventState.PENDING;
        if (paid == null) paid = false;
        if (requestModeration == null) requestModeration = true;
        if (participantLimit == null) participantLimit = 0;
        if (confirmedRequests == null) confirmedRequests = 0;
        if (views == null) views = 0;
    }
}