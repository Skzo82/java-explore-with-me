package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("""
       SELECT e FROM Event e
       WHERE e.state = ru.practicum.main.model.EventState.PUBLISHED
         AND (:text IS NULL OR 
              lower(e.annotation) LIKE lower(concat('%', :text, '%')) OR
              lower(e.description) LIKE lower(concat('%', :text, '%')))
         AND (:paid IS NULL OR e.paid = :paid)
         AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart)
         AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
         AND (:categories IS NULL OR e.category.id IN :categories)
       """)
    Page<Event> searchPublic(@Param("text") String text,
                             @Param("categories") List<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd,
                             Pageable pageable);
}