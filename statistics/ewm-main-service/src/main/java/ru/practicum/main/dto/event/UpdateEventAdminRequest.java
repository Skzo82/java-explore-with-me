package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/* # DTO для частичного обновления события администратором */
@Getter
@Setter
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;         // может быть null

    @Size(min = 20, max = 7000)
    private String description;        // может быть null

    @Size(min = 3, max = 120)
    private String title;              // может быть null

    private LocationDto location;      // новая локация (опционально)
    private Long category;             // id новой категории (опционально)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // новая дата (опционально)

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;        // PUBLISH_EVENT / REJECT_EVENT
}