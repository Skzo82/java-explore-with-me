package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.event.*;
import ru.practicum.main.dto.user.UserShortDto;
import ru.practicum.main.model.*;

import java.time.LocalDateTime;

/* # Маппер для сущности Event и её DTO */
@UtilityClass
public class EventMapper {

    /* # Создание новой сущности Event из NewEventDto */
    public Event toNew(NewEventDto dto, User initiator, Category category) {
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
        e.setState(EventState.PENDING); // # новое событие всегда в состоянии PENDING
        if (dto.getLocation() != null) {
            e.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        }
        return e;
    }

    /* # Краткое DTO события */
    public EventShortDto toShort(Event e) {
        CategoryDto categoryDto = null;
        if (e.getCategory() != null) {
            categoryDto = CategoryDto.builder()
                    .id(e.getCategory().getId())
                    .name(e.getCategory().getName())
                    .build();
        }

        UserShortDto initiatorDto = null;
        if (e.getInitiator() != null) {
            initiatorDto = UserShortDto.builder()
                    .id(e.getInitiator().getId())
                    .name(e.getInitiator().getName())
                    .build();
        }

        Integer confirmed = e.getConfirmedRequests();
        if (confirmed == null) {
            confirmed = 0; // # защита от NPE
        }

        Integer views = e.getViews();
        if (views == null) {
            views = 0;
        }

        return EventShortDto.builder()
                .id(e.getId())
                .annotation(e.getAnnotation())
                .title(e.getTitle())
                .eventDate(e.getEventDate())
                .paid(Boolean.TRUE.equals(e.getPaid()))
                .confirmedRequests(confirmed)
                .views(views)
                .category(categoryDto)
                .initiator(initiatorDto)
                .commentsCount(0L)
                .build();
    }

    /* # Полное DTO события */
    public EventFullDto toFull(Event e) {
        LocationDto loc = null;
        if (e.getLocation() != null) {
            loc = LocationDto.builder()
                    .lat(e.getLocation().getLat())
                    .lon(e.getLocation().getLon())
                    .build();
        }

        CategoryDto categoryDto = null;
        if (e.getCategory() != null) {
            categoryDto = CategoryDto.builder()
                    .id(e.getCategory().getId())
                    .name(e.getCategory().getName())
                    .build();
        }

        UserShortDto initiatorDto = null;
        if (e.getInitiator() != null) {
            initiatorDto = UserShortDto.builder()
                    .id(e.getInitiator().getId())
                    .name(e.getInitiator().getName())
                    .build();
        }

        Integer confirmed = e.getConfirmedRequests();
        if (confirmed == null) {
            confirmed = 0;
        }

        Integer views = e.getViews();
        if (views == null) {
            views = 0;
        }

        return EventFullDto.builder()
                .id(e.getId())
                .annotation(e.getAnnotation())
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .createdOn(e.getCreatedOn())
                .publishedOn(e.getPublishedOn())
                .paid(Boolean.TRUE.equals(e.getPaid()))
                .participantLimit(e.getParticipantLimit())
                .requestModeration(Boolean.TRUE.equals(e.getRequestModeration()))
                .title(e.getTitle())
                .state(e.getState() != null ? e.getState().name() : null)
                .category(categoryDto)
                .initiator(initiatorDto)
                .confirmedRequests(confirmed)
                .views(views)
                .location(loc)
                .commentsCount(0L)
                .build();
    }
}