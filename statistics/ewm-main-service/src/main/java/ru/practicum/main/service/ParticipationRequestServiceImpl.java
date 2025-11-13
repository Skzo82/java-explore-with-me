package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.ParticipationRequestMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.RequestStatus;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository
        .UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto create(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (event.getInitiator() != null && event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator cannot request participation in own event");
        }

        if (requestRepository.existsByEvent_IdAndRequester_Id(eventId, userId)) {
            throw new ConflictException("Request already exists for this user and event");
        }

        int limit = event.getParticipantLimit(); // int, non Integer
        if (limit > 0) {
            long confirmed = requestRepository.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (confirmed >= limit) {
                throw new ConflictException("Participant limit has been reached");
            }
        }

        boolean autoConfirm = !Boolean.TRUE.equals(event.getRequestModeration()) || limit == 0;

        ParticipationRequest req = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .status(autoConfirm ? RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();

        req = requestRepository.save(req);
        return ParticipationRequestMapper.toDto(req);
    }

    @Override
    public List<ParticipationRequestDto> findAll(long userId) {
        return requestRepository.findAllByRequester_IdOrderByCreatedDesc(userId)
                .stream()
                .map(ParticipationRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(long userId, long requestId) {
        ParticipationRequest req = requestRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request not found: " + requestId));
        req.setStatus(RequestStatus.CANCELED);
        req = requestRepository.save(req);
        return ParticipationRequestMapper.toDto(req);
    }
}