package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/* # Сущность заявки на участие в событии */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "participation_requests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "requester_id"}))
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // # идентификатор заявки

    /* # Событие, на которое подана заявка */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /* # Пользователь, который подал заявку */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    /* # Дата и время создания заявки */
    @Column(nullable = false)
    private LocalDateTime created;

    /* # Текущий статус заявки */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RequestStatus status;
}