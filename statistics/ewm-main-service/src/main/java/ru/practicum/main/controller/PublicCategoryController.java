package ru.practicum.main.controller.publicapi;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "10") @Positive int size) {
        return categoryService.findAll(PageRequest.of(from / size, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto get(@PathVariable Long catId) {
        return ((ru.practicum.main.service.CategoryServiceImpl) categoryService).get(catId);
    }
}