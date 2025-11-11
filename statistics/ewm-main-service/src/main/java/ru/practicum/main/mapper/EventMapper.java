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
            Double lat = dto.getLocation().getLat();
            Double lon = dto.getLocation().getLon();
            e.setLocation(new Location(
                    lat == null ? null : lat.floatValue(),
                    lon == null ? null : lon.floatValue()
            ));
        }
        return e;
    }

    public static EventShortDto toShort(Event e) {
        return EventShortDto.builder()
                .id(e.getId())
                .annotation(e.getAnnotation())
                .categoryId(e.getCategory() != null ? e.getCategory().getId() : null)
                .eventDate(e.getEventDate())
                .title(e.getTitle())
                .paid(e.isPaid())
                .views(e.getViews())
                .build();
    }

    public static EventFullDto toFull(Event e) {
        LocationDto loc = null;
        if (e.getLocation() != null) {
            Float lat = e.getLocation().getLat();
            Float lon = e.getLocation().getLon();
            loc = LocationDto.builder()
                    .lat(lat == null ? null : lat.doubleValue())
                    .lon(lon == null ? null : lon.doubleValue())
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