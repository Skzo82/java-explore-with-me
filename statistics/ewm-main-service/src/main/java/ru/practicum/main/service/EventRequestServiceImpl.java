package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.dto.request.ParticipationRequestStatusUpdateResult;
import ru.practicum.main.dto.request.UpdateEventRequestStatusRequest;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.ParticipationRequestMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.RequestStatus;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* # Реализация сервиса управления заявками инициатора события */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventRequestServiceImpl implements EventRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getEventRequests(long userId, long eventId) {
        Event event = getOwnedEvent(userId, eventId);

        return requestRepository.findAllByEvent_Id(event.getId())
                .stream()
                .map(ParticipationRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestStatusUpdateResult updateEventRequestsStatus(
            long userId,
            long eventId,
            UpdateEventRequestStatusRequest dto
    ) {
        Event event = getOwnedEvent(userId, eventId);

        if (dto.getRequestIds() == null || dto.getRequestIds().isEmpty()) {
            // # Нечего обновлять – пустой результат
            return new ParticipationRequestStatusUpdateResult(List.of(), List.of());
        }

        // # Загружаем все заявки по id
        List<ParticipationRequest> requests = requestRepository.findAllById(dto.getRequestIds());
        if (requests.size() != dto.getRequestIds().size()) {
            throw new NotFoundException("Some requests not found");
        }

        // # Проверяем, что все заявки относятся к данному событию
        Map<Long, ParticipationRequest> byId = requests.stream()
                .collect(Collectors.toMap(ParticipationRequest::getId, r -> r));

        for (Long reqId : dto.getRequestIds()) {
            ParticipationRequest r = byId.get(reqId);
            if (r == null || r.getEvent() == null || !r.getEvent().getId().equals(eventId)) {
                throw new NotFoundException("Request " + reqId + " does not belong to event " + eventId);
            }
        }

        // # Обновлять можно только PENDING
        for (ParticipationRequest r : requests) {
            if (r.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Only pending requests can be changed");
            }
        }

        int limit = event.getParticipantLimit() == null ? 0 : event.getParticipantLimit();
        int currentConfirmed = event.getConfirmedRequests() == null ? 0 : event.getConfirmedRequests();

        String status = dto.getStatus();
        if (status == null) {
            throw new ConflictException("Status is required");
        }

        List<ParticipationRequestDto> confirmedDtos = new ArrayList<>();
        List<ParticipationRequestDto> rejectedDtos = new ArrayList<>();

        if ("CONFIRMED".equals(status)) {
            // # Если лимит уже исчерпан – сразу 409
            if (limit > 0 && currentConfirmed >= limit) {
                throw new ConflictException("Participant limit has been reached");
            }

            for (ParticipationRequest r : requests) {
                if (limit > 0 && currentConfirmed >= limit) {
                    // # Дальнейшие подтверждения при достигнутом лимите -> 409
                    throw new ConflictException("Participant limit has been reached");
                }
                r.setStatus(RequestStatus.CONFIRMED);
                currentConfirmed++;
                confirmedDtos.add(ParticipationRequestMapper.toDto(r));
            }

        } else if ("REJECTED".equals(status)) {
            for (ParticipationRequest r : requests) {
                r.setStatus(RequestStatus.REJECTED);
                rejectedDtos.add(ParticipationRequestMapper.toDto(r));
            }
        } else {
            throw new ConflictException("Unknown status: " + status);
        }

        requestRepository.saveAll(requests);
        event.setConfirmedRequests(currentConfirmed);
        eventRepository.save(event);

        return new ParticipationRequestStatusUpdateResult(confirmedDtos, rejectedDtos);
    }

    /* # Проверка владения событием: только инициатор может управлять заявками */
    private Event getOwnedEvent(long userId, long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (event.getInitiator() == null || !event.getInitiator().getId().equals(userId)) {
            // # Чужое событие -> 404
            throw new NotFoundException("Event not found for user: " + eventId);
        }
        return event;
    }
}