package ru.practicum.main.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {

    @Size(max = 50)
    private String title;

    private Boolean pinned;

    private Set<Long> events;
}