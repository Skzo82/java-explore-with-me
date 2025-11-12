package ru.practicum.main.controller.privateapi;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Validated
public class ParticipationRequestController {

    private final ParticipationRequestService service;

    // POST /users/{userId}/requests?eventId=...
    @PostMapping
    public ParticipationRequestDto create(@PathVariable @Positive Long userId,
                                          @RequestParam("eventId") @Positive Long eventId) {
        return service.create(userId, eventId);
    }

    // GET /users/{userId}/requests
    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable @Positive Long userId) {
        return service.findAll(userId);
    }

    // PATCH /users/{userId}/requests/{requestId}/cancel
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Long userId,
                                          @PathVariable @Positive Long requestId) {
        return service.cancel(userId, requestId);
    }
}