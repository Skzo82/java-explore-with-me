package ru.practicum.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.dto.comment.NewCommentDto;
import ru.practicum.main.dto.comment.UpdateCommentDto;
import ru.practicum.main.service.CommentService;

import java.util.List;

/* # Контроллер для работы с комментариями к событиям */
@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    /* # Создание комментария пользователем */
    @PostMapping("/users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable Long userId,
            @Valid @RequestBody NewCommentDto newCommentDto
    ) {
        return commentService.createComment(userId, newCommentDto);
    }

    /* # Обновление собственного комментария пользователя */
    @PatchMapping("/users/{userId}/comments")
    public CommentDto updateComment(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateCommentDto updateDto
    ) {
        return commentService.updateComment(userId, updateDto);
    }

    /* # Удаление собственного комментария пользователя */
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long userId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(userId, commentId);
    }

    /* # Публичное получение комментария по id */
    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    /* # Публичное получение списка комментариев к событию */
    @GetMapping("/comments")
    public List<CommentDto> getCommentsByEvent(
            @RequestParam Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return commentService.getCommentsByEvent(eventId, from, size);
    }
}