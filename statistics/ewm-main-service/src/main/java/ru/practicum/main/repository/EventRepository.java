package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("""
                SELECT e FROM Event e
                WHERE e.state = ru.practicum.main.model.EventState.PUBLISHED
                  AND (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%',:text,'%'))
                       OR LOWER(e.description) LIKE LOWER(CONCAT('%',:text,'%')))
                  AND (:paid IS NULL OR e.paid = :paid)
                  AND (:start IS NULL OR e.eventDate >= :start)
                  AND (:end IS NULL OR e.eventDate <= :end)
                  AND (:cats IS NULL OR e.category.id IN :cats)
            """)
    Page<Event> searchPublic(@Param("text") String text,
                             @Param("cats") Collection<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             Pageable pageable);
}