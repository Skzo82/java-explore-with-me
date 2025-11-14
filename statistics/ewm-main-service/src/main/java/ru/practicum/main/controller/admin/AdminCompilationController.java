package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.NewCompilationDto;
import ru.practicum.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.CompilationService;

import java.util.List;

/* # Админ-эндпоинты для подборок */
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService service;

    /* # Создание подборки -> 201 Created */
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return service.create(dto);
    }

    /* # Частичное обновление -> 200 OK */
    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable @Positive long compId,
                                 @Valid @RequestBody UpdateCompilationRequest dto) {
        return service.update(compId, dto);
    }

    /* # Удаление -> 204 No Content */
    @DeleteMapping("/{compId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long compId) {
        service.delete(compId);
    }

    /* # Список (для админ-проверок) — from/size с дефолтом 10 */
    @GetMapping
    public List<CompilationDto> list(@RequestParam(required = false) Boolean pinned,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return service.findAllPublic(pinned, pageable);
    }
}