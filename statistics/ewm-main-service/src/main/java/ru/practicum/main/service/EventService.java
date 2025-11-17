package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.event.*;

import java.time.LocalDateTime;
import java.util.List;

/* # Контракт сервиса событий */
public interface EventService {

    // ----- Private (owner) -----
    EventFullDto create(Long userId, NewEventDto dto);

    List<EventShortDto> getUserEvents(Long userId, Pageable pageable);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest dto);

    EventFullDto cancelByUser(Long userId, Long eventId);

    // ----- Public -----
    List<EventShortDto> getPublicEvents(PublicEventFilterDto filter, Pageable pageable);

    EventFullDto getPublishedEventById(Long eventId);

    // ----- Admin -----
    List<EventFullDto> searchAdmin(AdminEventFilterDto filter, Pageable pageable);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto);
}