package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.model.Comment;

import java.util.List;

/* # Репозиторий для работы с комментариями */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /* # Все комментарии к событию по убыванию даты создания */
    List<Comment> findAllByEventIdOrderByCreatedDesc(Long eventId);

    /* # Пагинированный список комментариев по возрастанию даты */
    Page<Comment> findAllByEventIdOrderByCreatedAsc(Long eventId, Pageable pageable);

    /* # Количество комментариев для одного события */
    long countByEventId(Long eventId);

    /* # Проекция для количества комментариев по событиям */
    interface EventCommentsCount {
        Long getEventId();

        Long getCount();
    }

    /* # Количество комментариев для набора событий (bulk-запрос) */
    @Query("""
            select c.event.id as eventId,
                   count(c.id) as count
            from Comment c
            where c.event.id in :eventIds
            group by c.event.id
            """)
    List<EventCommentsCount> countByEventIdIn(@Param("eventIds") List<Long> eventIds);
}