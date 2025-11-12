package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/* # DTO для создания нового события */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {

    /* # Аннотация: 20..2000 символов */
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    /* # Описание: 20..7000 символов */
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    /* # Дата события (обязательно в будущем) */
    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /* # Локация обязательна */
    @NotNull
    private LocationDto location;

    /* # Платное ли событие (по умолчанию false) */
    @Builder.Default
    private Boolean paid = false;

    /* # Ограничение участников (>=0, по умолчанию 0) */
    @Builder.Default
    @PositiveOrZero
    private Integer participantLimit = 0;

    /* # Требуется ли модерация заявок (по умолчанию true) */
    @Builder.Default
    private Boolean requestModeration = true;

    /* # Заголовок: 3..120 символов */
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    /* # Категория обязательна (id) */
    @NotNull
    private Long category;
}