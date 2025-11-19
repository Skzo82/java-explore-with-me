package ru.practicum.main.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/* # Полное представление комментария */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;                 // идентификатор комментария
    private String text;             // текст комментария

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;   // дата создания

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;   // дата обновления

    private Long authorId;           // идентификатор автора
    private String authorName;       // имя автора
}