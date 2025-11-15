package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    /* # Публичный поиск событий без фильтрации по категориям */
    @Query("""
            SELECT e FROM Event e
            WHERE e.state = ru.practicum.main.model.EventState.PUBLISHED
              AND (
                    :text IS NULL
                 OR e.annotation LIKE CONCAT('%', :text, '%')
                 OR e.description LIKE CONCAT('%', :text, '%')
              )
              AND (:paid IS NULL OR e.paid = :paid)
              AND (e.eventDate >= COALESCE(:rangeStart, e.eventDate))
              AND (e.eventDate <= COALESCE(:rangeEnd, e.eventDate))
            """)
    Page<Event> searchPublicWithoutCategories(@Param("text") String text,
                                              @Param("paid") Boolean paid,
                                              @Param("rangeStart") LocalDateTime rangeStart,
                                              @Param("rangeEnd") LocalDateTime rangeEnd,
                                              Pageable pageable);

    /* # Публичный поиск событий с фильтрацией по категориям */
    @Query("""
            SELECT e FROM Event e
            WHERE e.state = ru.practicum.main.model.EventState.PUBLISHED
              AND (
                    :text IS NULL
                 OR e.annotation LIKE CONCAT('%', :text, '%')
                 OR e.description LIKE CONCAT('%', :text, '%')
              )
              AND (:paid IS NULL OR e.paid = :paid)
              AND (e.eventDate >= COALESCE(:rangeStart, e.eventDate))
              AND (e.eventDate <= COALESCE(:rangeEnd, e.eventDate))
              AND e.category.id IN :categories
            """)
    Page<Event> searchPublicWithCategories(@Param("text") String text,
                                           @Param("categories") List<Long> categories,
                                           @Param("paid") Boolean paid,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           Pageable pageable);
}
