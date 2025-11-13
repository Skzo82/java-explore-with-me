package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;

import java.util.List;

/* # Сервис подборок (компиляций) */
public interface CompilationService {

    /* # Создать подборку */
    CompilationDto create(NewCompilationDto dto);

    /* # Частично обновить подборку */
    CompilationDto update(long compId, UpdateCompilationRequest dto);

    /* # Удалить подборку */
    void delete(long compId);

    /* # Закрепить/открепить подборку */
    void setPinned(long compId, boolean pinned);

    /* # Управление событиями внутри подборки */
    void addEvent(long compId, long eventId);

    void removeEvent(long compId, long eventId);

    /* # Публичные методы */
    List<CompilationDto> findAllPublic(Boolean pinned, Pageable pageable);

    CompilationDto getByIdPublic(long compId);
}