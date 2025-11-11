package ru.practicum.main.dto.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private String description;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private Long category;
    private Long initiator;
    private Integer confirmedRequests;
    private Integer views;
    private String state;
    private LocationDto location;
}