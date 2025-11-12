package ru.practicum.main.controller.privateapi;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {

    private final ParticipationRequestService requests;

    public ParticipationRequestController(ParticipationRequestService requests) {
        this.requests = requests;
    }

    /* create -> 201 */
    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable @Positive long userId,
                                                          @RequestParam @Positive long eventId) {
        ParticipationRequestDto created = requests.create(userId, eventId);
        return ResponseEntity.created(URI.create("/users/" + userId + "/requests/" + created.getId()))
                .body(created);
    }

    /* list -> 200 */
    @GetMapping
    public List<ParticipationRequestDto> list(@PathVariable @Positive long userId) {
        return requests.findAll(userId);
    }

    /* cancel -> 200 */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long requestId) {
        return requests.cancel(userId, requestId);
    }
}