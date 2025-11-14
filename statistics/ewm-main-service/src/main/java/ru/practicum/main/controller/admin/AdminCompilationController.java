package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService service;

    /* CREATE */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return service.create(dto);
    }

    /* UPDATE */
    @PatchMapping("/{compId}")
    public CompilationDto update(
            @PathVariable long compId,
            @Valid @RequestBody UpdateCompilationRequest dto
    ) {
        return service.update(compId, dto);
    }

    /* DELETE compilation */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        service.delete(compId);
    }

    /* ADD EVENT */
    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEvent(
            @PathVariable long compId,
            @PathVariable long eventId
    ) {
        service.addEvent(compId, eventId);
    }

    /* REMOVE EVENT */
    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEvent(
            @PathVariable long compId,
            @PathVariable long eventId
    ) {
        service.removeEvent(compId, eventId);
    }

    /* LIST */
    @GetMapping
    public List<CompilationDto> list(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        int page = from / size;
        return service.findAllPublic(pinned, org.springframework.data.domain.PageRequest.of(page, size));
    }
}