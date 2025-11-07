package ru.practicum.stats.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false, length = 1024)
    private String uri;

    @Column(nullable = false, length = 45)
    private String ip;

    @Column(name = "ts", nullable = false)
    private LocalDateTime timestamp;
}