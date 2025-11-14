package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/* # DTO частичного обновления события владельцем.
   # Все поля опциональны; @Size допускает null. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequest {

    /* # Ограничения соответствуют созданию, но поле может быть null */
    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    private String title;

    /* # id категории (опционально) */
    private Long category;

    /* # Действие владельца: SEND_TO_REVIEW | CANCEL_REVIEW */
    private UserStateAction stateAction;

    public enum UserStateAction {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }
}