// src/main/java/ru/practicum/main/repository/ParticipationRequestRepository.java
package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    boolean existsByEvent_IdAndRequester_Id(long eventId, long requesterId);

    long countByEvent_IdAndStatus(long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByRequester_IdOrderByCreatedDesc(long requesterId);

    Optional<ParticipationRequest> findByIdAndRequester_Id(long requestId, long requesterId);

    List<ParticipationRequest> findAllByEvent_Id(long eventId);
}