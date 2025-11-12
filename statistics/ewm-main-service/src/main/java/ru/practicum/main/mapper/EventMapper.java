package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.LocationDto;
import ru.practicum.main.dto.event.NewEventDto;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    /* # Создание новой сущности события из DTO */
    public static Event toNew(NewEventDto dto, User initiator, Category category) {
        Event e = new Event();
        e.setAnnotation(dto.getAnnotation());
        e.setDescription(dto.getDescription());
        e.setEventDate(dto.getEventDate());
        e.setCreatedOn(LocalDateTime.now());
        e.setPaid(Boolean.TRUE.equals(dto.getPaid()));
        e.setParticipantLimit(dto.getParticipantLimit() == null ? 0 : dto.getParticipantLimit());
        e.setRequestModeration(Boolean.TRUE.equals(dto.getRequestModeration()));
        e.setTitle(dto.getTitle());
        e.setInitiator(initiator);
        e.setCategory(category);
        if (dto.getLocation() != null) {
            // --> ВАЖНО: используем Double, без конверсий к Float
            e.setLocation(new Location(
                    dto.getLocation().getLat(),
                    dto.getLocation().getLon()
            ));
        }
        return e;
    }

    /* # Короткое представление события */
    public static EventShortDto toShort(Event e) {
        return EventShortDto.builder()
                .id(e.getId())
                .annotation(e.getAnnotation())
                .title(e.getTitle())
                .eventDate(e.getEventDate())
                .paid(e.isPaid())
                .views(e.getViews())
                .categoryId(e.getCategory() != null ? e.getCategory().getId() : null)
                .build();
    }

    /* # Полное представление события */
    public static EventFullDto toFull(Event e) {
        LocationDto loc = null;
        if (e.getLocation() != null) {
            // --> ВАЖНО: источник тоже Double, возвращаем Double
            loc = LocationDto.builder()
                    .lat(e.getLocation().getLat())
                    .lon(e.getLocation().getLon())
                    .build();
        }

        return EventFullDto.builder()
                .id(e.getId())
                .annotation(e.getAnnotation())
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .createdOn(e.getCreatedOn())
                .publishedOn(e.getPublishedOn())
                .paid(e.isPaid())
                .participantLimit(e.getParticipantLimit())
                .requestModeration(e.isRequestModeration())
                .title(e.getTitle())
                .state(e.getState() != null ? e.getState().name() : null)
                .initiator(e.getInitiator() != null ? e.getInitiator().getId() : null)
                .category(e.getCategory() != null ? e.getCategory().getId() : null)
                .views(e.getViews())
                .location(loc)
                .build();
    }
}