package ru.practicum.main.dto.compilation;

import lombok.*;
import ru.practicum.main.dto.event.EventShortDto;

import java.util.List;

/* # DTO подборки, возвращаемый наружу */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {

        private Long id;                    // # идентификатор подборки
        private String title;               // # заголовок
        @Builder.Default
        private Boolean pinned = false;     // # закреплена ли на главной
        @Builder.Default
        private List<EventShortDto> events = List.of(); // # события в подборке (короткие)
}