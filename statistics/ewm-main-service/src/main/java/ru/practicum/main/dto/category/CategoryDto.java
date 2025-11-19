package ru.practicum.main.dto.category;

import lombok.*;

/* # DTO категории для ответов */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;    // # идентификатор
    private String name; // # имя категории
}