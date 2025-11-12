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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventRequestServiceImpl implements EventRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getEventRequests(long userId, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Only initiator can view participation requests");
        }

        return requestRepository.findAllByEvent_Id(eventId).stream()
                .map(ParticipationRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestStatusUpdateResult updateEventRequestsStatus(
            long userId, long eventId, UpdateEventRequestStatusRequest dto) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Only initiator can change participation requests");
        }

        List<ParticipationRequest> requests = requestRepository.findAllById(dto.getRequestIds());
        ParticipationRequestStatusUpdateResult result = new ParticipationRequestStatusUpdateResult();

        long confirmedCount = requestRepository.findAllByEvent_Id(eventId).stream()
                .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                .count();

        for (ParticipationRequest r : requests) {
            if (r.getStatus() != RequestStatus.PENDING) continue;

            if ("CONFIRMED".equalsIgnoreCase(dto.getStatus())) {
                if (event.getParticipantLimit() == 0 || confirmedCount < event.getParticipantLimit()) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    confirmedCount++;
                    result.getConfirmedRequests().add(ParticipationRequestMapper.toDto(r));
                } else {
                    r.setStatus(RequestStatus.REJECTED);
                    result.getRejectedRequests().add(ParticipationRequestMapper.toDto(r));
                }
            } else if ("REJECTED".equalsIgnoreCase(dto.getStatus())) {
                r.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(ParticipationRequestMapper.toDto(r));
            }
        }

        requestRepository.saveAll(requests);
        return result;
    }
}