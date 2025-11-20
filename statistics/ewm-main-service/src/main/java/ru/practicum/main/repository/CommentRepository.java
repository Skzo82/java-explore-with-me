package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Comment;

import java.util.List;

/* # Репозиторий для работы с комментариями */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /* # Все комментарии к событию по убыванию даты создания */
    List<Comment> findAllByEventIdOrderByCreatedDesc(Long eventId);

    /* # Пагинированный список комментариев по возрастанию даты */
    Page<Comment> findAllByEventIdOrderByCreatedAsc(Long eventId, Pageable pageable);

    /* # Количество комментариев для события */
    long countByEventId(Long eventId);
}