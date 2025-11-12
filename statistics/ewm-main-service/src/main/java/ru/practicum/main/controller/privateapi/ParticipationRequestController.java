package ru.practicum.main.controller.privateapi;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Validated
public class ParticipationRequestController {

    private final ParticipationRequestService requests;

    /* # Создать заявку на участие в событии -> 201 */
    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable @Positive Long userId,
                                                          @RequestParam("eventId") @Positive Long eventId) {
        ParticipationRequestDto created = requests.create(userId, eventId);
        return ResponseEntity
                .created(URI.create("/users/" + userId + "/requests/" + created.getId()))
                .body(created);
    }

    /* # Список заявок пользователя -> 200 */
    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable @Positive Long userId) {
        return requests.findAll(userId);
    }

    /* # Отменить свою заявку -> 200 */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Long userId,
                                          @PathVariable @Positive Long requestId) {
        return requests.cancel(userId, requestId);
    }
}