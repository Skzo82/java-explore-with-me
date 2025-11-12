package ru.practicum.main.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/* # DTO для частичного обновления категории (PATCH) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCategoryRequest {

    /* # Имя категории может быть обновлено, не пустое, до 128 символов */
    @NotBlank
    @Size(max = 128)
    private String name;
}