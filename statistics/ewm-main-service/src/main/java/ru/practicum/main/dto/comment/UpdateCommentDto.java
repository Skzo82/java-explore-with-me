package ru.practicum.main.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/* # DTO для обновления комментария */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDto {

    /* # Идентификатор комментария, который обновляем */
    @NotNull
    private Long commentId;

    /* # Новый текст комментария */
    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;
}