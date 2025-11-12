package ru.practicum.main.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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

    // GET /categories
    @GetMapping
    public List<CategoryDto> list(Pageable pageable) {
        return categories.findAll(pageable);
    }

    // GET /categories/{id}
    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable @Positive long id) {
        return categories.getById(id);
    }
}