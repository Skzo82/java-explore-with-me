package ru.practicum.main.dto.event;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/* # DTO для частичного обновления события пользователем */
@Getter
@Setter
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000, message = "Annotation length must be between 20 and 2000")
    private String annotation; // может быть null

    @Size(min = 20, max = 7000, message = "Description length must be between 20 and 7000")
    private String description; // может быть null

    @Size(min = 3, max = 120, message = "Title length must be between 3 and 120")
    private String title; // может быть null

    private LocationDto location;      // новая локация (опционально)
    private Long category;             // id новой категории (опционально)
    private LocalDateTime eventDate;
    private Boolean paid;

    // # без аннотаций: проверяем в сервисе вручную
    private Integer participantLimit;

    private Boolean requestModeration;

    /* # Действие пользователя: SEND_TO_REVIEW / CANCEL_REVIEW */
    private UserStateAction stateAction;
}