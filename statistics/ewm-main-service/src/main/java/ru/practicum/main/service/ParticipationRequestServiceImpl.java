package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.request.ParticipationRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    @Override
    public ParticipationRequestDto create(long userId, long eventId) {
        // TODO implementare logica reale
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findAll(long userId) {
        // TODO implementare logica reale
        return List.of();
    }

    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        // TODO implementare logica reale
        return null;
    }
}