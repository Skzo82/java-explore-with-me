package ru.practicum.main.service;

import ru.practicum.main.dto.request.ParticipationRequestDto;

import java.util.List;

/* # Сервис заявок на участие */
public interface ParticipationRequestService {

    /* # Создать заявку на участие */
    ParticipationRequestDto create(long userId, long eventId);

    /* # Получить все заявки пользователя */
    List<ParticipationRequestDto> findAll(long userId);

    /* # Отменить свою заявку */
    ParticipationRequestDto cancel(long userId, long requestId);
}