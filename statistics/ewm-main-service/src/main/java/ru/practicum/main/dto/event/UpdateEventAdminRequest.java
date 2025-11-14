package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/* # DTO частичного обновления события администратором.
   # Все поля опциональны; @Size не требует @NotNull (null допустим). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 20, max = 7000)
    private String description;

    /* # Формат даты строго по ТЗ */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /* # id категории (опционально) */
    private Long category;

    /* # Координаты (опционально целиком) */
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    private String title;

    /* # Действие администратора: PUBLISH_EVENT | REJECT_EVENT */
    private AdminStateAction stateAction;

    public enum AdminStateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }
}