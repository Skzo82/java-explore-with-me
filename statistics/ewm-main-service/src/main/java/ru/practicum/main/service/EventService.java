package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.NewEventDto;
import ru.practicum.main.dto.event.UpdateEventAdminRequest;
import ru.practicum.main.dto.event.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

/* # Контракт сервиса событий (унифицировано: LocalDateTime + Pageable) */
public interface EventService {

    // ----- Private (owner) -----
    EventFullDto create(Long userId, NewEventDto dto);

    List<EventShortDto> getUserEvents(Long userId, Pageable pageable);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest dto);

    EventFullDto cancelByUser(Long userId, Long eventId);

    // ----- Public -----
    List<EventShortDto> getPublicEvents(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        Pageable pageable);

    EventFullDto getPublishedEventById(Long eventId);

    // ----- Admin -----
    List<EventFullDto> searchAdmin(List<Long> users,
                                   List<String> states,
                                   List<Long> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto);
}