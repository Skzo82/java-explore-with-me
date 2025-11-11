package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.event.*;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    // ----- Private (owner) -----

    @Override
    public EventFullDto create(Long userId, NewEventDto dto) {
        User initiator = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Category cat = categoryRepo.findById(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));

        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Event date must be at least 2 hours in the future");
        }

        Event e = EventMapper.toNew(dto, initiator, cat);
        return EventMapper.toFull(eventRepo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long userId, Pageable pageable) {
        return eventRepo.findAllByInitiatorId(userId, pageable)
                .map(EventMapper::toShort)
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event e = getOwned(userId, eventId);
        return EventMapper.toFull(e);
    }

    @Override
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest dto) {
        Event e = getOwned(userId, eventId);
        if (e.getState() == EventState.PUBLISHED) {
            throw new IllegalStateException("Published event can’t be edited");
        }
        applyUserUpdate(e, dto);
        return EventMapper.toFull(eventRepo.save(e));
    }

    @Override
    public EventFullDto cancelByUser(Long userId, Long eventId) {
        Event e = getOwned(userId, eventId);
        e.setState(EventState.CANCELED);
        return EventMapper.toFull(eventRepo.save(e));
    }

    // ----- Public -----

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getPublicEvents(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Pageable pageable) {
        return eventRepo.searchPublic(
                        text,
                        (categories == null || categories.isEmpty()) ? null : categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        pageable)
                .map(EventMapper::toShort)
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublishedEventById(Long eventId) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if (e.getState() != EventState.PUBLISHED) {
            throw new IllegalStateException("Event is not published");
        }
        return EventMapper.toFull(e);
    }

    // ----- Admin -----

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> searchAdmin(List<Long> users,
                                          List<String> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          Pageable pageable) {
        // Минимальная реализация: фильтрация в памяти
        return eventRepo.findAll(pageable).stream()
                .filter(e -> users == null || users.isEmpty() || users.contains(e.getInitiator().getId()))
                .filter(e -> states == null || states.isEmpty() || states.contains(e.getState().name()))
                .filter(e -> categories == null || categories.isEmpty() || categories.contains(e.getCategory().getId()))
                .filter(e -> (rangeStart == null || !e.getEventDate().isBefore(rangeStart)) &&
                        (rangeEnd == null || !e.getEventDate().isAfter(rangeEnd)))
                .map(EventMapper::toFull)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        applyAdminUpdate(e, dto);

        if ("PUBLISH_EVENT".equals(dto.getStateAction())) {
            if (e.getState() != EventState.PENDING)
                throw new IllegalStateException("Only pending can be published");
            e.setState(EventState.PUBLISHED);
            e.setPublishedOn(LocalDateTime.now());
        } else if ("REJECT_EVENT".equals(dto.getStateAction())) {
            if (e.getState() == EventState.PUBLISHED)
                throw new IllegalStateException("Cannot reject published");
            e.setState(EventState.CANCELED);
        }

        return EventMapper.toFull(eventRepo.save(e));
    }

    // ----- Helpers -----

    private Event getOwned(Long userId, Long eventId) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if (!e.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("User " + userId + " is not the owner of event " + eventId);
        }
        return e;
    }

    private void applyUserUpdate(Event e, UpdateEventUserRequest dto) {
        if (dto.getAnnotation() != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) e.setEventDate(dto.getEventDate());

        if (dto.getLocation() != null) {
            Double lat = dto.getLocation().getLat();
            Double lon = dto.getLocation().getLon();
            e.setLocation(new Location(
                    lat == null ? null : lat.floatValue(),
                    lon == null ? null : lon.floatValue()
            ));
        }

        if (dto.getPaid() != null) e.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) e.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) e.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) e.setTitle(dto.getTitle());

        if (dto.getCategory() != null) {
            Category cat = categoryRepo.findById(dto.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));
            e.setCategory(cat);
        }

        if ("CANCEL_REVIEW".equals(dto.getStateAction())) e.setState(EventState.CANCELED);
        if ("SEND_TO_REVIEW".equals(dto.getStateAction())) e.setState(EventState.PENDING);
    }

    private void applyAdminUpdate(Event e, UpdateEventAdminRequest dto) {
        if (dto.getAnnotation() != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) e.setEventDate(dto.getEventDate());

        if (dto.getLocation() != null) {
            Double lat = dto.getLocation().getLat();
            Double lon = dto.getLocation().getLon();
            e.setLocation(new Location(
                    lat == null ? null : lat.floatValue(),
                    lon == null ? null : lon.floatValue()
            ));
        }

        if (dto.getPaid() != null) e.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) e.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) e.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) e.setTitle(dto.getTitle());

        if (dto.getCategory() != null) {
            Category cat = categoryRepo.findById(dto.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));
            e.setCategory(cat);
        }
    }
}