package ru.practicum.main.service;

import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.dto.comment.NewCommentDto;

import java.util.List;

/* # Сервис для управления комментариями */
public interface CommentService {

    CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto updateDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    CommentDto getCommentById(Long commentId);

    /* # Получение всех комментариев события без пагинации (если где-то нужно) */
    List<CommentDto> getCommentsByEventId(Long eventId);

    /* # Пагинированный список комментариев (публичный эндпоинт) */
    List<CommentDto> getCommentsByEvent(Long eventId, int from, int size);
}