package ru.practicum.main.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/* # Результат массового обновления заявок: отдельные списки подтверждённых/отклонённых */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestStatusUpdateResult {

    @Builder.Default
    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

    @Builder.Default
    private List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}