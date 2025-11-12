package ru.practicum.main.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

// ru.practicum.main.dto.compilation.NewCompilationDto
public class NewCompilationDto {
    // название подборки обязательно, <= 50
    @NotBlank
    @Size(max = 50)
    private String title;

    private Boolean pinned;           // может быть null
    private Set<Long> events;         // может быть null/пусто
}