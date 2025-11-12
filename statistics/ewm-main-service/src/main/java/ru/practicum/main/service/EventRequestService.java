package ru.practicum.main.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.dto.request.ParticipationRequestStatusUpdateResult;
import ru.practicum.main.dto.request.UpdateEventRequestStatusRequest;

import java.util.List;

/* # Сервис заявок владельца события (управление запросами на участие) */
@Validated
public interface EventRequestService {
    List<ParticipationRequestDto> getEventRequests(long userId, long eventId);

    ParticipationRequestStatusUpdateResult updateEventRequestsStatus(long userId, long eventId,
                                                                     UpdateEventRequestStatusRequest dto);
}