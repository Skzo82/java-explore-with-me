package ru.practicum.main.controller.privateapi;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class ParticipationRequestController {

    private final ParticipationRequestService service;

    // POST /users/{userId}/requests?eventId=...
    @PostMapping
    public ParticipationRequestDto create(@PathVariable @Positive long userId,
                                          @RequestParam(name = "eventId") @Positive Long eventId) {
        return service.create(userId, eventId);
    }

    // GET /users/{userId}/requests
    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable @Positive long userId) {
        return service.findAll(userId);
    }

    // PATCH /users/{userId}/requests/{requestId}/cancel
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long requestId) {
        return service.cancel(userId, requestId);
    }
}