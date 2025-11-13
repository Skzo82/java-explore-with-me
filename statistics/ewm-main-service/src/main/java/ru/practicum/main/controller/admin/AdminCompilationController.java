package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.CompilationService;


@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService compilations;

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return compilations.create(dto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable @Positive long compId,
                                 @Valid @RequestBody UpdateCompilationRequest dto) {
        return compilations.update(compId, dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long compId) {
        compilations.delete(compId);
    }

    @PatchMapping("/{compId}/pin")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void setPinned(@PathVariable @Positive long compId,
                          @RequestParam boolean pinned) {
        compilations.setPinned(compId, pinned);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void addEvent(@PathVariable @Positive long compId,
                         @PathVariable @Positive long eventId) {
        compilations.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void removeEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        compilations.removeEvent(compId, eventId);
    }
}