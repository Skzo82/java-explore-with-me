package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.CompilationService;

import java.net.URI;

/* # Админ-эндпоинты для управления подборками */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService compilations;

    /* # Создать подборку -> 201 */
    @PostMapping
    public ResponseEntity<CompilationDto> create(@Valid @RequestBody NewCompilationDto dto) {
        CompilationDto created = compilations.create(dto);
        return ResponseEntity
                .created(URI.create("/compilations/" + created.getId()))
                .body(created);
    }

    /* # Обновить подборку (частично) -> 200 */
    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable @Positive Long compId,
                                 @Valid @RequestBody UpdateCompilationRequest dto) {
        return compilations.update(compId, dto);
    }

    /* # Удалить подборку -> 204 */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long compId) {
        compilations.delete(compId);
    }

    /* # Закрепить подборку на главной -> 204 */
    @PatchMapping("/{compId}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pin(@PathVariable @Positive Long compId) {
        compilations.setPinned(compId, true);
    }

    /* # Снять закреп -> 204 */
    @DeleteMapping("/{compId}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unpin(@PathVariable @Positive Long compId) {
        compilations.setPinned(compId, false);
    }

    /* # Добавить событие в подборку -> 204 */
    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEvent(@PathVariable @Positive Long compId,
                         @PathVariable @Positive Long eventId) {
        compilations.addEvent(compId, eventId);
    }

    /* # Удалить событие из подборки -> 204 */
    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEvent(@PathVariable @Positive Long compId,
                            @PathVariable @Positive Long eventId) {
        compilations.removeEvent(compId, eventId);
    }
}