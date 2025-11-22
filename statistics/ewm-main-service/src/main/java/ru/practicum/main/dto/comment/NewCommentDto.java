package ru.practicum.main.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/* # DTO для создания комментария */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCommentDto {

    /* # Идентификатор события, к которому оставляют комментарий */
    @NotNull
    private Long eventId;

    /* # Текст комментария */
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(max = 1000, message = "Максимальная длина комментария — 1000 символов")
    private String text;
}