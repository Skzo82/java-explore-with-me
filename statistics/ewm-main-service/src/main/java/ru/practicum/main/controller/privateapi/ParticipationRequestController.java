package ru.practicum.main.controller.privateapi;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.util.List;

/* # Приватные эндпоинты заявок на участие текущего пользователя */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Validated
public class ParticipationRequestController {

    private final ParticipationRequestService service;

    /* # Получить все свои заявки */
    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable @Positive long userId) {
        return service.findAll(userId);
    }

    /* # Добавить заявку на участие в событии
       Требуем обязательный query param eventId -> при отсутствии вернётся 400 */
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable @Positive long userId,
                                          @RequestParam("eventId") @Positive Long eventId) {
        return service.create(userId, eventId);
    }

    /* # Отменить свою заявку — вернуть JSON dto, не text/plain */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long requestId) {
        return service.cancel(userId, requestId);
    }
}