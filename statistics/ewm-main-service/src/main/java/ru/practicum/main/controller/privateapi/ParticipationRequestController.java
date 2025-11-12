package ru.practicum.main.controller.privateapi;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.util.List;

/* # Приватные эндпоинты: работа пользователя со своими заявками на участие */
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class ParticipationRequestController {

    private final ParticipationRequestService requests;

    /* # Создать заявку на участие в событии */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable @Positive long userId,
                                          @RequestParam("eventId") @Positive long eventId) {
        return requests.create(userId, eventId);
    }

    /* # Получить все свои заявки */
    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable @PositiveOrZero long userId) {
        return requests.findAll(userId);
    }

    /* # Отменить свою заявку */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long requestId) {
        return requests.cancel(userId, requestId);
    }
}