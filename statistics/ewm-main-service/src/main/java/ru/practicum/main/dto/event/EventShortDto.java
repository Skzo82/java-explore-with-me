package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.user.UserShortDto;

import java.time.LocalDateTime;

/* # Краткое представление события (используется в списках) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {

    private Long id;                 // идентификатор
    private String annotation;       // краткое описание
    private String title;            // заголовок

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // дата события

    private Boolean paid;            // платное ли событие
    private Integer confirmedRequests; // подтверждённые заявки
    private Integer views;           // просмотры

    private CategoryDto category;    // категория (DTO) — по спецификации
    private UserShortDto initiator;  // инициатор (DTO) — по спецификации
}