package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.model.ParticipationRequest;

/* # Простой mapper заявок */
@UtilityClass
public class ParticipationRequestMapper {

    public static ParticipationRequestDto toDto(ParticipationRequest r) {
        if (r == null) return null;
        return ParticipationRequestDto.builder()
                .id(r.getId())
                .event(r.getEvent().getId())
                .requester(r.getRequester().getId())
                .created(r.getCreated())
                .status(r.getStatus().name())
                .build();
    }
}
