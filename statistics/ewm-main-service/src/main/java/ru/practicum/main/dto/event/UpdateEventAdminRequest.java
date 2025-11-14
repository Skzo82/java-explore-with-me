package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/* # Запрос админа на обновление события */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {

    /* # Аннотация: 20..2000 символов */
    @Size(min = 20, max = 2000)
    private String annotation;

    /* # Описание: 20..7000 символов */
    @Size(min = 20, max = 7000)
    private String description;

    /* # Дата события в формате ТЗ */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /* # Идентификатор категории */
    private Long category;

    /* # Координаты */
    private LocationDto location;

    /* # Платность */
    private Boolean paid;

    /* # Лимит участников (>= 0) */
    @PositiveOrZero
    private Integer participantLimit;

    /* # Нужна ли модерация заявок */
    private Boolean requestModeration;

    /* # Заголовок: 3..120 символов */
    @Size(min = 3, max = 120)
    private String title;

    /* # Действие со статусом: PUBLISH_EVENT | REJECT_EVENT */
    private String stateAction;
}