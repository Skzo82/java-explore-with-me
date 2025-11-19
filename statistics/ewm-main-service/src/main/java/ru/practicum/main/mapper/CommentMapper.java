package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.dto.comment.NewCommentDto;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;

import java.time.LocalDateTime;

/* # Маппер для комментариев */
@UtilityClass
public class CommentMapper {

    /* # Создать сущность комментария из DTO и связанных сущностей */
    public Comment toComment(NewCommentDto dto, User author, Event event) {
        return Comment.builder()
                .text(dto.getText())
                .author(author)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }

    /* # Перевести сущность комментария в DTO */
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .build();
    }
}