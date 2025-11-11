package ru.practicum.main.dto.event;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {
    private String annotation;
    private String description;
    private LocalDateTime eventDate;
    private Long category;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private String stateAction; // "PUBLISH_EVENT" | "REJECT_EVENT"
}