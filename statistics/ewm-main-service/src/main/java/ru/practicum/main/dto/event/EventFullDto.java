package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.user.UserShortDto;

import java.time.LocalDateTime;

/* # Полное представление события (ответы API) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {

    private Long id;                      // идентификатор события
    private String annotation;            // краткое описание
    private String description;           // полное описание

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;      // дата события

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;      // дата создания

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;    // дата публикации (для PUBLISHED)

    private Boolean paid;                 // платное ли событие
    private Integer participantLimit;     // лимит участников
    private Boolean requestModeration;    // модерация заявок
    private String title;                 // заголовок события

    private CategoryDto category;         // категория
    private UserShortDto initiator;       // инициатор

    private Integer confirmedRequests;    // подтверждённые заявки
    private Integer views;                // просмотры
    private String state;                 // PENDING / PUBLISHED / CANCELED

    private LocationDto location;         // координаты

    /* # Количество комментариев к событию */
    private Long commentsCount;
}