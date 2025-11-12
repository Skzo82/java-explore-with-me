package ru.practicum.main.service;

import ru.practicum.main.dto.request.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto create(long userId, long eventId);

    List<ParticipationRequestDto> findAll(long userId);

    ParticipationRequestDto cancel(long userId, long requestId);
}