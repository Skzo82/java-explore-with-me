package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.CompilationMapper;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    /* CREATE */
    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        Set<Event> events = (dto.getEvents() == null || dto.getEvents().isEmpty())
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(eventRepository.findAllById(dto.getEvents()));

        Compilation saved = compilationRepository.save(CompilationMapper.fromNew(dto, events));
        return CompilationMapper.toDto(saved);
    }

    /* UPDATE (частичный) */
    @Override
    @Transactional
    public CompilationDto update(long compId, UpdateCompilationRequest dto) {
        Compilation c = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));

        Set<Event> newEvents = null;
        if (dto.getEvents() != null) {
            newEvents = new LinkedHashSet<>(eventRepository.findAllById(dto.getEvents()));
        }

        CompilationMapper.applyUpdate(c, dto, newEvents);
        Compilation updated = compilationRepository.save(c);
        return CompilationMapper.toDto(updated);
    }

    /* DELETE */
    @Override
    @Transactional
    public void delete(long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation not found: " + compId);
        }
        compilationRepository.deleteById(compId);
    }

    /* PIN/UNPIN */
    @Override
    @Transactional
    public void setPinned(long compId, boolean pinned) {
        Compilation c = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));
        c.setPinned(pinned);
        compilationRepository.save(c);
    }

    /* ADD EVENT */
    @Override
    @Transactional
    public void addEvent(long compId, long eventId) {
        Compilation c = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));
        Event e = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        c.getEvents().add(e);
        compilationRepository.save(c);
    }

    /* REMOVE EVENT */
    @Override
    @Transactional
    public void removeEvent(long compId, long eventId) {
        Compilation c = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));
        c.getEvents().removeIf(ev -> ev.getId().equals(eventId));
        compilationRepository.save(c);
    }

    /* PUBLIC LIST */
    @Override
    public List<CompilationDto> findAllPublic(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationRepository.findAll(pageable)
                    .map(CompilationMapper::toDto)
                    .getContent();
        }
        return compilationRepository.findAllByPinned(pinned, pageable)
                .map(CompilationMapper::toDto)
                .getContent();
    }

    /* PUBLIC GET */
    @Override
    public CompilationDto getByIdPublic(long compId) {
        Compilation c = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));
        return CompilationMapper.toDto(c);
    }
}