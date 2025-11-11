package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    private String annotation;

    @NotBlank
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Integer participantLimit = 0;

    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank
    private String title;

    @NotNull
    private Long category;
}