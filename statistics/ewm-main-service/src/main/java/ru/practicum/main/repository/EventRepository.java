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

    boolean existsByCategoryId(Long categoryId);

    /* # Публичный поиск событий (без фильтра по категориям) */
    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = ru.practicum.main.model.EventState.PUBLISHED
              AND (
                     :text IS NULL
                     OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
                     OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))
                  )
              AND (:paid IS NULL OR e.paid = :paid)
              AND e.eventDate >= :rangeStart
              AND e.eventDate <= :rangeEnd
            """)
    Page<Event> searchPublicNoCategories(@Param("text") String text,
                                         @Param("paid") Boolean paid,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         Pageable pageable);

    /* # Публичный поиск событий (с фильтром по категориям) */
    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = ru.practicum.main.model.EventState.PUBLISHED
              AND (
                     :text IS NULL
                     OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
                     OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))
                  )
              AND (:paid IS NULL OR e.paid = :paid)
              AND e.eventDate >= :rangeStart
              AND e.eventDate <= :rangeEnd
              AND e.category.id IN :categories
            """)
    Page<Event> searchPublicWithCategories(@Param("text") String text,
                                           @Param("categories") List<Long> categories,
                                           @Param("paid") Boolean paid,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           Pageable pageable);
}