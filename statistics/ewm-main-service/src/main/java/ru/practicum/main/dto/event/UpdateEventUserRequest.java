package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/* # DTO для частичного обновления события пользователем */
@Getter
@Setter
public class UpdateEventUserRequest {

    /* # Краткая аннотация: от 20 до 2000 символов (если передана) */
    @Size(min = 20, max = 2000)
    private String annotation;        // может быть null

    /* # Полное описание: от 20 до 7000 символов (если передано) */
    @Size(min = 20, max = 7000)
    private String description;       // может быть null

    /* # Заголовок: от 3 до 120 символов (если передан) */
    @Size(min = 3, max = 120)
    private String title;             // может быть null

    private Long category;            // id категории (опционально)
    private LocationDto location;     // новая локация (опционально)

    /* # Новая дата события (если передана) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    /* # Действие пользователя: SEND_TO_REVIEW / CANCEL_REVIEW (опционально) */
    private UserStateAction stateAction;
}