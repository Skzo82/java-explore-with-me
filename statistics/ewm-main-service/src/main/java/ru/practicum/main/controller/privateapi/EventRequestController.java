package ru.practicum.main.controller.privateapi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.dto.request.ParticipationRequestStatusUpdateResult;
import ru.practicum.main.dto.request.UpdateEventRequestStatusRequest;
import ru.practicum.main.service.EventRequestService;

import java.util.List;

/* # Приватные эндпоинты: владелец события управляет заявками на участие */
@RestController
@RequestMapping("/users/{userId}/events/{eventId}/requests")
@RequiredArgsConstructor
@Validated
public class EventRequestController {

    private final EventRequestService eventRequests;

    /* # Получить заявки на конкретное событие пользователя */
    @GetMapping
    public List<ParticipationRequestDto> getEventRequests(@PathVariable @Positive long userId,
                                                          @PathVariable @Positive long eventId) {
        return eventRequests.getEventRequests(userId, eventId);
    }

    /* # Массово подтвердить/отклонить заявки на событие */
    @PatchMapping
    public ParticipationRequestStatusUpdateResult updateStatuses(@PathVariable @Positive long userId,
                                                                 @PathVariable @Positive long eventId,
                                                                 @Valid @RequestBody UpdateEventRequestStatusRequest dto) {
        return eventRequests.updateEventRequestsStatus(userId, eventId, dto);
    }
}