package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.dto.comment.NewCommentDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.CommentMapper;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.EventState;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/* # Реализация сервиса комментариев */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        // # Проверяем пользователя и опубликованное событие
        User author = getUser(userId);
        Event event = getPublishedEvent(eventId);

        Comment comment = CommentMapper.toComment(newCommentDto, author, event);
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toCommentDto(saved);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId,
                                    Long eventId,
                                    Long commentId,
                                    NewCommentDto updateDto) {
        Comment comment = getComment(commentId);

        // # Пользователь может редактировать только свой комментарий к этому событию
        if (!comment.getAuthor().getId().equals(userId)
                || !comment.getEvent().getId().equals(eventId)) {
            throw new ConflictException("Пользователь не может изменить этот комментарий");
        }

        comment.setText(updateDto.getText());
        comment.setUpdated(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        return CommentMapper.toCommentDto(updated);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        Comment comment = getComment(commentId);

        // # Пользователь может удалить только свой комментарий к этому событию
        if (!comment.getAuthor().getId().equals(userId)
                || !comment.getEvent().getId().equals(eventId)) {
            throw new ConflictException("Пользователь не может удалить этот комментарий");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = getComment(commentId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId) {
        // # Убеждаемся, что событие существует и опубликовано
        getPublishedEvent(eventId);

        return commentRepository.findAllByEventIdOrderByCreatedDesc(eventId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    /* # Пагинированный список комментариев к событию */
    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, int from, int size) {
        // # Убеждаемся, что событие существует и опубликовано
        getPublishedEvent(eventId);

        // # from/size -> номер страницы
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> pageResult =
                commentRepository.findAllByEventIdOrderByCreatedAsc(eventId, pageable);

        return pageResult
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    /* ===== Вспомогательные методы ===== */

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User not found: " + userId));
    }

    private Event getPublishedEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("Event not found: " + eventId));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя комментировать неопубликованное событие");
        }
        return event;
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("Comment not found: " + commentId));
    }
}