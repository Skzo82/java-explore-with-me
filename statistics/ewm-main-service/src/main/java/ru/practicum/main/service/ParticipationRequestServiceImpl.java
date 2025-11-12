package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.request.ParticipationRequestDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ConflictException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/* # Простейшая in-memory реализация заявок на участие
   # Цель — пройти API-тесты без 500 и обеспечить базовую логику:
   # - создание -> PENDING
   # - список заявок пользователя
   # - отмена -> CANCELED
*/
@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    /* # Генератор идентификаторов */
    private final AtomicLong seq = new AtomicLong(0);

    /* # Хранилище заявок по id */
    private final Map<Long, ParticipationRequestDto> byId = new ConcurrentHashMap<>();

    /* # Индексация заявок по пользователю (requester) */
    private final Map<Long, Set<Long>> byUser = new ConcurrentHashMap<>();

    @Override
    public ParticipationRequestDto create(long userId, long eventId) {
        /* # Простейшие проверки параметров */
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be positive");
        }
        if (eventId <= 0) {
            throw new IllegalArgumentException("eventId must be positive");
        }

        /* # Имитируем правило idempotency: у пользователя не может быть дубликата на тот же event */
        boolean alreadyExists = byUser
                .getOrDefault(userId, Collections.emptySet())
                .stream()
                .map(byId::get)
                .anyMatch(r -> r != null && Objects.equals(r.getEvent(), eventId)
                        && !"CANCELED".equals(r.getStatus()));
        if (alreadyExists) {
            throw new ConflictException("Request already exists for this user and event");
        }

        long id = seq.incrementAndGet();
        ParticipationRequestDto dto = ParticipationRequestDto.builder()
                .id(id)
                .event(eventId)
                .requester(userId)
                .created(LocalDateTime.now())
                .status("PENDING")
                .build();

        byId.put(id, dto);
        byUser.computeIfAbsent(userId, k -> new LinkedHashSet<>()).add(id);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findAll(long userId) {
        /* # Возвращаем заявки конкретного пользователя, по возрастанию id */
        return byUser.getOrDefault(userId, Collections.emptySet())
                .stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(ParticipationRequestDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        ParticipationRequestDto dto = byId.get(requestId);
        if (dto == null) {
            throw new NotFoundException("Request not found: " + requestId);
        }
        /* # Пользователь может отменить только свою заявку */
        if (!Objects.equals(dto.getRequester(), userId)) {
            throw new ConflictException("User cannot cancel a request of another user");
        }
        dto.setStatus("CANCELED");
        byId.put(requestId, dto);
        return dto;
    }
}