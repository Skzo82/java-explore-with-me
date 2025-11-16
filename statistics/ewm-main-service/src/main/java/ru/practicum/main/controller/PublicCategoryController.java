package ru.practicum.main.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {

    private final CategoryService categories;

    /* # Получение категорий с поддержкой from/size и значением size по умолчанию = 10 */
    @GetMapping
    public List<CategoryDto> list(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categories.findAll(pageable);
    }

    // GET /categories/{id}
    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable @Positive long id) {
        return categories.getById(id);
    }
}