package ru.practicum.main.dto.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/* # Краткое представление события */
@Data
@Builder
public class EventShortDto {
    private Long id;
    private String annotation;
    private String title;
    private LocalDateTime eventDate;

    private Boolean paid;
    private Integer views;

    /* # Идентификатор категории — используется в маппере EventMapper.toShort() */
    private Long categoryId;
}