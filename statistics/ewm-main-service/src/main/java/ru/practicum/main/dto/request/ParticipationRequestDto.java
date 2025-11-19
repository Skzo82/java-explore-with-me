package ru.practicum.main.dto.request;

import lombok.*;
import java.time.LocalDateTime;

/* # DTO заявки на участие в событии */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {

    private Long id;
    private Long event;
    private Long requester;
    private LocalDateTime created;
    private String status; // пример: PENDING, CONFIRMED, REJECTED, CANCELED
}