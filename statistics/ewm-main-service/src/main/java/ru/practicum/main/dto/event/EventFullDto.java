package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.user.UserShortDto;

import java.time.LocalDateTime;

/* # Полное представление события (используется в ответах API) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {

    private Long id;                     // идентификатор события
    private String annotation;           // краткое описание
    private String description;          // полное описание

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;     // дата события

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;     // дата создания

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;   // дата публикации (для PUBLISHED)

    private Boolean paid;                // платное ли событие
    private Integer participantLimit;    // лимит участников
    private Boolean requestModeration;   // требуется ли модерация запросов
    private String title;                // заголовок события

    private CategoryDto category;        // категория (DTO)
    private UserShortDto initiator;      // инициатор (DTO с id+name)

    private Integer confirmedRequests;   // подтверждённые заявки
    private Integer views;               // просмотры
    private String state;                // PENDING, PUBLISHED, CANCELED

    private LocationDto location;        // координаты
}