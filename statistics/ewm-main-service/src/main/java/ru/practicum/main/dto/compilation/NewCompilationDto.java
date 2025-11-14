package ru.practicum.main.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/* # DTO создания подборки событий */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {

    /* # Заголовок обязателен и не пустой (до 50) */
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    /* # Признак закрепления (по умолчанию false) */
    @Builder.Default
    private Boolean pinned = false;

    /* # Набор id событий (не обязателен) */
    private Set<Long> events;
}