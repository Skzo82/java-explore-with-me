package ru.practicum.main.dto.event;

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

    private Long id;                       // идентификатор события
    private String annotation;             // краткое описание
    private String description;            // полное описание
    private LocalDateTime eventDate;       // дата события
    private LocalDateTime createdOn;       // дата создания
    private LocalDateTime publishedOn;     // дата публикации (для PUBLISHED)
    private Boolean paid;                  // платное ли событие
    private Integer participantLimit;      // лимит участников
    private Boolean requestModeration;     // требуется ли модерация запросов
    private String title;                  // заголовок события

    private CategoryDto category;          // категория (DTO, не id)
    private UserShortDto initiator;        // инициатор события (DTO с id+name)

    private Integer confirmedRequests;     // количество подтверждённых заявок
    private Integer views;                 // просмотры
    private String state;                  // состояние события (PENDING, PUBLISHED, CANCELED)
    private LocationDto location;          // координаты
}