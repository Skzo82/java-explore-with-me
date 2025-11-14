package ru.practicum.main.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/* # DTO частичного обновления категории (PATCH) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCategoryRequest {

    /* # Имя категории обязательно при PATCH, до 50 символов */
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}