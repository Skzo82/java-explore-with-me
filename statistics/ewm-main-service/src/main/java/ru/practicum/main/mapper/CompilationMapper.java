package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/* # Маппер для сущности Compilation */
@UtilityClass
public class CompilationMapper {

    /* # Доменная -> DTO (короткие события внутри) */
    public static CompilationDto toDto(Compilation c) {
        return CompilationDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .pinned(Boolean.TRUE.equals(c.getPinned()))
                .events(c.getEvents() == null
                        ? List.of()
                        : c.getEvents().stream()
                        .map(EventMapper::toShort)
                        .collect(Collectors.toList()))
                .build();
    }

    /* # Создание доменной сущности из NewCompilationDto + набор событий */
    public static Compilation fromNew(NewCompilationDto dto, Set<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(Boolean.TRUE.equals(dto.getPinned()))
                .events(events == null ? new LinkedHashSet<>() : new LinkedHashSet<>(events))
                .build();
    }

    /* # Патч-обновление существующей компиляции */
    public static void applyUpdate(Compilation c, UpdateCompilationRequest dto, Set<Event> newEvents) {
        if (dto == null) return;
        if (StringUtils.hasText(dto.getTitle())) {
            c.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            c.setPinned(dto.getPinned());
        }
        if (newEvents != null) {
            c.setEvents(new LinkedHashSet<>(newEvents));
        }
    }

    /* # Утилита: список событий -> Set */
    public static Set<Event> toEventSet(List<Event> events) {
        return events == null ? new LinkedHashSet<>() : new LinkedHashSet<>(events);
    }
}