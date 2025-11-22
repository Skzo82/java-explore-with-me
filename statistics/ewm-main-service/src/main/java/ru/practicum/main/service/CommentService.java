package ru.practicum.main.service;

import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.dto.comment.NewCommentDto;
import ru.practicum.main.dto.comment.UpdateCommentDto;

import java.util.List;
import java.util.Map;

/* # Сервис для управления комментариями */
public interface CommentService {

    CommentDto createComment(Long userId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, UpdateCommentDto updateDto);

    void deleteComment(Long userId, Long commentId);

    CommentDto getCommentById(Long commentId);

    /* # Получение всех комментариев события без пагинации (если где-то нужно) */
    List<CommentDto> getCommentsByEventId(Long eventId);

    /* # Пагинированный список комментариев (публичный эндпоинт) */
    List<CommentDto> getCommentsByEvent(Long eventId, int from, int size);

    /* # Количество комментариев к одному событию (для детального просмотра) */
    long getCommentsCountForEvent(Long eventId);

    /* # Количество комментариев для набора событий (bulk-вариант, чтобы избежать N+1) */
    Map<Long, Long> getCommentsCountForEvents(List<Long> eventIds);
}