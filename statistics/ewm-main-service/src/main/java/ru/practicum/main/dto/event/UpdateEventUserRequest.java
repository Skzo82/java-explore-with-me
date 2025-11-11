package ru.practicum.main.dto.event;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequest {
    private String annotation;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private Long category;
    private String stateAction; // "SEND_TO_REVIEW" | "CANCEL_REVIEW"
}