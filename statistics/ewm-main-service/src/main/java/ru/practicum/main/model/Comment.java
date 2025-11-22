package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/* # Комментарий к событию */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // идентификатор комментария

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;             // событие, к которому оставлен комментарий

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;             // автор комментария

    @Column(name = "text", nullable = false, length = 1000)
    private String text;             // текст комментария

    @Column(name = "created", nullable = false)
    private LocalDateTime created;   // дата создания

    @Column(name = "updated")
    private LocalDateTime updated;   // дата последнего обновления (может быть null)
}