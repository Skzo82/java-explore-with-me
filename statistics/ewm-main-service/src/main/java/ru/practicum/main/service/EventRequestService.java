package ru.practicum.main.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.dto.request.ParticipationRequestStatusUpdateResult;
import ru.practicum.main.dto.request.UpdateEventRequestStatusRequest;

import java.util.List;

/* # Сервис заявок владельца события (управление запросами на участие) */
@Validated
public interface EventRequestService {

    /* # Получить все заявки на участие в своём событии */
    List<ParticipationRequestDto> getEventRequests(long userId, long eventId);

    /* # Подтвердить/отклонить заявки на участие в своём событии */
    ParticipationRequestStatusUpdateResult updateEventRequestsStatus(
            long userId,
            long eventId,
            UpdateEventRequestStatusRequest dto
    );
}