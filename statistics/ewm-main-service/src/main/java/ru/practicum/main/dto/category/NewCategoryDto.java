package ru.practicum.main.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/* # DTO создания категории */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategoryDto {

    /* # Имя категории: обязательно, не пустое, до 50 символов */
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}