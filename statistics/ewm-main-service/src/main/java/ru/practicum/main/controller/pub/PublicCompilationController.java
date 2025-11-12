package ru.practicum.main.controller.pub;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.service.CompilationService;

import java.util.List;

/* # Публичные эндпоинты подборок (компиляций) */
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
@Validated
public class PublicCompilationController {

    private final CompilationService compilations;

    /* # Получить список подборок с фильтром по "pinned" и пагинацией -> 200 */
    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilations.findAllPublic(pinned, pageable);
    }

    /* # Получить одну подборку по id -> 200 */
    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable @Positive Long compId) {
        return compilations.getByIdPublic(compId);
    }
}