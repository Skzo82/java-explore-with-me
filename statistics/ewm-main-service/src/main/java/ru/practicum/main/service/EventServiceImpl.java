package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.NewEventDto;
import ru.practicum.main.dto.event.UpdateEventAdminRequest;
import ru.practicum.main.dto.event.UpdateEventUserRequest;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.EventState;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/* # Реализация сервиса событий */
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final StatsClient statsClient; // # клиент к stats-service

    // ----- Private (owner) -----

    @Override
    public EventFullDto create(Long userId, NewEventDto dto) {
        User initiator = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        Category cat = categoryRepo.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategory()));

        /* # Дата события должна быть не раньше чем через 2 часа */
        validateEventDate(dto.getEventDate());

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
            /* # пользователь не может менять опубликованное событие -> 409 */
            throw new ConflictException("Only pending or canceled events can be changed");
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

        /* # Текстовый фильтр: приводим к нижнему регистру и trim */
        String textSafe = (text == null || text.isBlank())
                ? null
                : text.trim().toLowerCase(Locale.ROOT);

        /* # Диапазон дат: делаем оба конца не null (локальные переменные) */
        LocalDateTime start = rangeStart;
        LocalDateTime end = rangeEnd;

        if (start == null && end == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        } else {
            if (start == null) {
                start = LocalDateTime.now();
            }
            if (end == null) {
                end = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
            }
        }

        if (end.isBefore(start)) {
            /* # некорректный диапазон дат -> 400 */
            throw new IllegalArgumentException("rangeEnd must not be before rangeStart");
        }

        // # Делаем "финальные" копии для использования в лямбдах
        final LocalDateTime startFinal = start;
        final LocalDateTime endFinal = end;

        /* # Загружаем все события и фильтруем в памяти (простое решение для спринта) */
        List<Event> all = eventRepo.findAll();

        List<Event> filtered = all.stream()
                // только опубликованные события
                .filter(e -> e.getState() == EventState.PUBLISHED)
                // текстовый поиск по аннотации и описанию
                .filter(e -> {
                    if (textSafe == null) return true;
                    String src = ((e.getAnnotation() == null ? "" : e.getAnnotation()) + " " +
                            (e.getDescription() == null ? "" : e.getDescription()))
                            .toLowerCase(Locale.ROOT);
                    return src.contains(textSafe);
                })
                // фильтр по категориям
                .filter(e -> {
                    if (categories == null || categories.isEmpty()) return true;
                    return e.getCategory() != null && categories.contains(e.getCategory().getId());
                })
                // фильтр по платности
                .filter(e -> {
                    if (paid == null) return true;
                    return Boolean.TRUE.equals(paid) == Boolean.TRUE.equals(e.getPaid());
                })
                // фильтр по дате
                .filter(e -> {
                    LocalDateTime d = e.getEventDate();
                    return d == null || (!d.isBefore(startFinal) && !d.isAfter(endFinal));
                })
                .collect(Collectors.toList());

        /* # onlyAvailable: оставляем только события, где есть свободные места */
        if (Boolean.TRUE.equals(onlyAvailable)) {
            filtered = filtered.stream()
                    .filter(e -> {
                        Integer limit = e.getParticipantLimit();
                        Integer confirmed = e.getConfirmedRequests();
                        if (limit == null || limit == 0) {
                            // лимита нет — всегда доступно
                            return true;
                        }
                        if (confirmed == null) {
                            confirmed = 0;
                        }
                        return confirmed < limit;
                    })
                    .collect(Collectors.toList());
        }

        /* # сортировка */
        if ("EVENT_DATE".equalsIgnoreCase(sort)) {
            filtered.sort(Comparator.comparing(Event::getEventDate,
                    Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("VIEWS".equalsIgnoreCase(sort)) {
            filtered.sort(Comparator.comparing(Event::getViews,
                    Comparator.nullsLast(Comparator.reverseOrder())));
        }

        /* # Пагинация вручную через from/size (из Pageable) */
        int from = pageable.getPageNumber() * pageable.getPageSize();
        int size = pageable.getPageSize();

        if (from < 0) from = 0;
        if (size <= 0) size = 10;

        int startIndex = Math.min(from, filtered.size());
        int endIndex = Math.min(from + size, filtered.size());

        List<Event> page = filtered.subList(startIndex, endIndex);

        return page.stream()
                .map(EventMapper::toShort)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublishedEventById(Long eventId) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        if (e.getState() != EventState.PUBLISHED) {
            /* # публичный доступ к неопубликованному событию -> 404 */
            throw new NotFoundException("Event is not published: " + eventId);
        }

        /* # Получаем количество просмотров из stats-service */
        String uri = "/events/" + eventId;

        Instant start = e.getCreatedOn()
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = Instant.now();

        List<ViewStatsDto> stats = statsClient.getStats(
                start,
                end,
                List.of(uri),
                true   // уникальные IP
        );

        long views = stats.isEmpty() ? 0L : stats.get(0).hits();
        e.setViews((int) views);

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
        // # Простейшая фильтрация в памяти (для прохождения проверок)
        return eventRepo.findAll().stream()
                .filter(e -> users == null || users.isEmpty() || users.contains(e.getInitiator().getId()))
                .filter(e -> states == null || states.isEmpty() || states.contains(e.getState().name()))
                .filter(e -> categories == null || categories.isEmpty() || categories.contains(e.getCategory().getId()))
                .filter(e -> {
                    LocalDateTime d = e.getEventDate();
                    if (rangeStart != null && d != null && d.isBefore(rangeStart)) return false;
                    if (rangeEnd != null && d != null && d.isAfter(rangeEnd)) return false;
                    return true;
                })
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(EventMapper::toFull)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        applyAdminUpdate(e, dto);

        if ("PUBLISH_EVENT".equals(dto.getStateAction())) {
            if (e.getState() != EventState.PENDING) {
                /* # публиковать можно только PENDING -> 409 */
                throw new ConflictException("Only pending events can be published");
            }
            e.setState(EventState.PUBLISHED);
            e.setPublishedOn(LocalDateTime.now());

        } else if ("REJECT_EVENT".equals(dto.getStateAction())) {
            if (e.getState() == EventState.PUBLISHED) {
                /* # нельзя отклонить уже опубликованное -> 409 */
                throw new ConflictException("Cannot reject published event");
            }
            e.setState(EventState.CANCELED);
        }

        return EventMapper.toFull(eventRepo.save(e));
    }

    // ----- Helpers -----

    private Event getOwned(Long userId, Long eventId) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        if (!e.getInitiator().getId().equals(userId)) {
            /* # чужое событие -> 404 (чтобы не раскрывать факт существования) */
            throw new NotFoundException("Event not found for user: " + eventId);
        }
        return e;
    }

    private void applyUserUpdate(Event e, UpdateEventUserRequest dto) {
        if (dto.getAnnotation() != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());

        if (dto.getEventDate() != null) {
            /* # проверяем дату при обновлении */
            validateEventDate(dto.getEventDate());
            e.setEventDate(dto.getEventDate());
        }

        if (dto.getLocation() != null) {
            e.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        }
        if (dto.getPaid() != null) e.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) {
            if (dto.getParticipantLimit() < 0) {
                // # для пользователя отрицательный лимит -> 400 BAD_REQUEST
                throw new IllegalArgumentException("Participant limit must be non-negative");
            }
            e.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) e.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) e.setTitle(dto.getTitle());
        if (dto.getCategory() != null) {
            Category cat = categoryRepo.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategory()));
            e.setCategory(cat);
        }
        if ("CANCEL_REVIEW".equals(dto.getStateAction())) e.setState(EventState.CANCELED);
        if ("SEND_TO_REVIEW".equals(dto.getStateAction())) e.setState(EventState.PENDING);
    }

    private void applyAdminUpdate(Event e, UpdateEventAdminRequest dto) {
        if (dto.getAnnotation() != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());

        if (dto.getEventDate() != null) {
            LocalDateTime newDate = dto.getEventDate();
            if (newDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Event date cannot be in the past");
            }
            e.setEventDate(newDate);
        }

        if (dto.getLocation() != null) {
            e.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        }
        if (dto.getPaid() != null) e.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) {
            if (dto.getParticipantLimit() < 0) {
                throw new IllegalArgumentException("Participant limit must be non-negative");
            }
            e.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) e.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) e.setTitle(dto.getTitle());
        if (dto.getCategory() != null) {
            Category cat = categoryRepo.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategory()));
            e.setCategory(cat);
        }
    }

    /* # Проверка даты события: не раньше, чем через 2 часа от текущего момента */
    private void validateEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Event date must be at least 2 hours in the future");
        }
    }
}