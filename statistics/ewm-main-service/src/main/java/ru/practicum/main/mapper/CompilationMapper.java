package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto toDto(Compilation c) {
        List<EventShortDto> events = c.getEvents() == null
                ? List.of()
                : c.getEvents().stream()
                .map(EventMapper::toShort) // <-- il tuo EventMapper espone toShort(...)
                .collect(Collectors.toList());

        return CompilationDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .pinned(Boolean.TRUE.equals(c.getPinned()))
                .events(events)
                .build();
    }

    public static Compilation fromNew(NewCompilationDto dto, Set<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(Boolean.TRUE.equals(dto.getPinned()))
                .events(events == null ? Set.of() : events)
                .build();
    }

    public static void applyUpdate(Compilation c, UpdateCompilationRequest dto, Set<Event> events) {
        if (dto.getTitle() != null) {
            c.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            c.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null) {
            c.setEvents(events == null ? Set.of() : events);
        }
    }
}